package com.zdk.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author zdk
 * @date 2022/5/1 17:43
 * 生产者
 */
public class Producer {

    private static final String QUEUE_NAME = "hello_world";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("211.69.238.77");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        //连接里创建信道
        Channel channel = connection.createChannel();

        //生成一个队列
        channel.queueDeclare(QUEUE_NAME,false,false, false, null);

        /**
         * 发送消息
         */
        String message;

        Scanner cin = new Scanner(System.in);

        while (cin.hasNext()){
            message = cin.next();
            //发布消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));

            System.out.println("消息发送完毕");
        }
    }
}
