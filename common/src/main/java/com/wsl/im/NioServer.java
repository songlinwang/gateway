package com.wsl.im;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wsl
 * @date 2019/7/24
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();
        new Thread(() -> {
            try {
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                listenerChannel.socket().bind(new InetSocketAddress(8000));
                // 是否阻塞
                listenerChannel.configureBlocking(false);
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
                while (true) {
                    // 检测是否有新的连接 1是超时时间 select方法返回当前连接数
                    if (serverSelector.select(1) > 0) {
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        set.stream().filter(key -> key.isAcceptable())
                                .collect(Collectors.toList())
                                .forEach(key -> {
                                    try {
                                        // 每次新来一个连接不需要创建新的线程，而是注册到clientSelector
                                        // 这样就不用像以前io模型每次新连接建立就是一个while循环
                                        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(clientSelector, SelectionKey.OP_ACCEPT);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                while (true) {
                    if (clientSelector.select(1) > 0) {
                        clientSelector.selectedKeys().stream().filter(key -> key.isReadable())
                                .collect(Collectors.toList())
                                .forEach(key -> {
                                    try {
                                        SocketChannel socketChannel = (SocketChannel) key.channel();
                                        ByteBuffer bf = ByteBuffer.allocate(1024);
                                        socketChannel.read(bf);
                                        bf.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(bf).toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                }
            } catch (Exception e) {

            }
        }).start();
    }
}
