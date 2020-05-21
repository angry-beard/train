package com.beard.train.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private int port;

    //轮询器  Selector 大堂经理
    private Selector selector;
    //缓冲器  Buffer   等候区
    private Buffer buffer = ByteBuffer.allocate(1024);

    public NIOServer(int port) {
        //初始化大堂经理
        try {
            this.port = port;
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(this.port));
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        System.out.println("listen on " + this.port);
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            key = socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            int len = channel.read((ByteBuffer) buffer);
            if (len > 0) {
                buffer.flip();
                String content = new String((byte[]) buffer.array(), 0, len);
                key = channel.register(selector, SelectionKey.OP_WRITE);
                key.attach(content);
                System.out.println("读取内容：" + content);
            }
        } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            String content = (String) key.attachment();
            channel.write(ByteBuffer.wrap(("输出：" + content).getBytes()));
            channel.close();
        }
    }

    public static void main(String[] args) {
        new NIOServer(8090).listen();
    }
}
