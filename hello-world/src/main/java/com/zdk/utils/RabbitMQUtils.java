package com.zdk.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author zdk
 * @date 2022/5/1 19:58
 */
@SuppressWarnings("all")
public class RabbitMQUtils {
    public static Channel getChannel() throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("211.69.238.77");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        Connection connection = connectionFactory.newConnection();

        return connection.createChannel();
    }
}
