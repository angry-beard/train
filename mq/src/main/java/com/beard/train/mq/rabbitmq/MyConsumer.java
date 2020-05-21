package com.beard.train.mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MyConsumer {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";
    private final static String QUEUE_NAME = "SIMPLE_QUEUE";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("106.14.119.243");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", false, false, null);
        System.out.println("等待消息、、、、、");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "com.angry.beard.best");
        Consumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("接收消息：" + msg + "  ");
                System.out.println("consumerTag：" + consumerTag + "  ");
                System.out.println("deliveryTag：" + envelope.getDeliveryTag() + "  ");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
