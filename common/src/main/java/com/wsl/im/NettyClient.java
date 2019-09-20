package com.wsl.im;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

/**
 * @author wsl
 * @date 2019/7/24
 */
public class NettyClient {
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        // NioSocketChannel对应于server的NioServerSockerChannel
        bootstrap
                // 1 执行线程模型
                .group(group)
                // 2 指定Io类型为NIO
                .channel(NioSocketChannel.class)
                // 3 IO处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringDecoder());
                        channel.writeAndFlush("hello");
                    }
                });
        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        while (true) {
            channel.writeAndFlush(": hello world");
            // 客户端可以用channel的attribute属性标识login。成功了则为true
            AttributeKey<Boolean> attributeKey = AttributeKey.newInstance("login");
            channel.attr(attributeKey).set(true);
            Thread.sleep(2000);
        }
    }

    public static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                Channel channel = ((ChannelFuture)future).channel();
                // 绑定成功后利用channel干一些事情
            } else if (retry == 0) {

            } else {
                // 第几次重连
                int order = MAX_RETRY - retry + 1;
                // 间隔
                int delay = 1 << order;
                bootstrap.config().group().schedule(() -> {
                            connect(bootstrap, host, port, retry);
                        },
                        delay, TimeUnit.SECONDS);
            }
        });
    }
}
