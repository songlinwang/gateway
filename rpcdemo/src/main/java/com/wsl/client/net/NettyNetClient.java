package com.wsl.client.net;

import com.wsl.discovery.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author wsl
 * @date 2019/7/1
 */
@Slf4j
public class NettyNetClient implements NetClient {

    private static Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    @Override
    public byte[] sendRequest(byte[] data, ServiceInfo serviceInfo) throws Throwable {
        String[] addInfoArray = serviceInfo.getAddress().split(":");
        byte[] respData = null;
        SendHandler sendHandler = new SendHandler(data);
        // 配置客户端
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            // 这个是禁止使用nagle算法
            // nagle算法是将小的数据包装成更大的进行发送 而不是一次发送一个 因此在数据包不足的时候 会等待其他数据到了 组装成大的数据包发送
            // 虽然该方法有效的改善了网络的有效负载 但是却造成了延时 。使用这个参数可以禁止nagle算法 这样子使用小数据即时传输可以更快
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline ch = socketChannel.pipeline();
                            ch.addLast(sendHandler);
                        }
                    });
            // ip: 端口
            bootstrap.connect(addInfoArray[0], Integer.valueOf(addInfoArray[1])).sync();
            respData = (byte[]) sendHandler.rspData();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放线程组资源
            eventLoopGroup.shutdownGracefully();
        }
        return respData;
    }

    private class SendHandler extends ChannelInboundHandlerAdapter {
        private CountDownLatch cdl = null;
        private byte[] data = null;
        private Object readMsg = null;

        public SendHandler(byte[] data) {
            cdl = new CountDownLatch(1);
            this.data = data;
        }

        public Object rspData() {
            try {
                cdl.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return readMsg;
        }

        /**
         * 当客户端connect成功后，服务器端就会收到
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.error("连接服务器成功");
            ByteBuf reqBuf = Unpooled.buffer(data.length);
            reqBuf.writeBytes(data);
            log.error("客户端发送消息");
            ctx.writeAndFlush(reqBuf);
        }

        /**
         * 当业务处理方发送回来消息的时候 就会到这个方法
         *
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.error("'client read msf:" + msg);
            try {
                ByteBuf msgBuf = (ByteBuf) msg;
                byte[] resp = new byte[((ByteBuf) msg).readableBytes()];
                msgBuf.readBytes(resp);
                readMsg = resp;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            logger.error("发生异常：" + cause.getMessage());
            ctx.close();
        }
    }
}
