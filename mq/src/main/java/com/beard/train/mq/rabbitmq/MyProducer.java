package com.beard.train.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MyProducer {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("106.14.119.243");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        for (int i = 0; i < 100; i++) {
            String msg = "hello world,Rabbit MQ! i=" + i;
            channel.basicPublish(EXCHANGE_NAME, "com.angry.beard.best", null, msg.getBytes());
        }
        channel.close();
        connection.close();
    }
}
