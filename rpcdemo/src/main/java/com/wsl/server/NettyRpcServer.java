package com.wsl.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wsl
 * @date 2019/7/2
 */
public class NettyRpcServer extends RpcServer {
    private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private Channel channel;

    public NettyRpcServer(int port, String protocol, RequestHandler requestHandler) {
        super(port, protocol, requestHandler);
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.ERROR)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    channelPipeline.addLast(new ChannelRequestHandler());
                }
            });
            ChannelFuture f = serverBootstrap.bind(port).sync();
            logger.info("完成服务端端口绑定和启动");
            channel = f.channel();
            // 等待服务通道关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            // Fixme 失败重试机制引入
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
        this.channel.close();
    }

    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("发生异常：" + cause.getMessage());
            ctx.flush();
            super.exceptionCaught(ctx, cause);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.info("激活：" + "服务器端");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.warn("服务器端收到消息");
            ByteBuf msgBuf = (ByteBuf) msg;
            byte[] req = new byte[msgBuf.readableBytes()];
            msgBuf.readBytes(req);
            byte[] res = handler.handlerRequest(req);
            logger.warn("发送响应");
            ByteBuf respBuf = Unpooled.buffer(res.length);
            respBuf.writeBytes(res);
            ctx.write(respBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
    }
}
