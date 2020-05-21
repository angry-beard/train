package com.beard.train.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOService {

    ServerSocket serverSocket;

    public BIOService(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听端口是：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getPort());
            InputStream is = socket.getInputStream();
            byte[] buff = new byte[1024];
            int len = is.read(buff);
            if (len > 0) {
                String msg = new String(buff, 0, len);
                System.out.println("收到：" + msg);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new BIOService(8989).listen();
    }
}
