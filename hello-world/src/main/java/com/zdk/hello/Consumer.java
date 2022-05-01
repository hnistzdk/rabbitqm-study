package com.zdk.hello;

import com.rabbitmq.client.*;

/**
 * @author zdk
 * @date 2022/5/1 17:43
 * 消费者
 */
public class Consumer {
    private static final String QUEUE_NAME = "hello_world";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("211.69.238.77");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        //连接里创建信道
        Channel channel = connection.createChannel();

        System.out.println("等待接收消息.........");

        //推送来的消息如何被消费的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println(msg);
        };

        //取消消费的一个回调接口  比如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> System.out.println("消息消费被中断");

        /**
         * 1.消费哪一个队列
         * 2.是否自动应答
         * 3.消费者如何进行消费的回调
         * 4.消费者取消了消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true,deliverCallback,cancelCallback);

    }
}
