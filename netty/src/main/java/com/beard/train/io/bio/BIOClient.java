package com.beard.train.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8090);
        OutputStream os = socket.getOutputStream();
        String name = UUID.randomUUID().toString();
        System.out.println("客户端发送数据:" + name);
        os.write(name.getBytes());
        os.close();
        socket.close();
    }
}
