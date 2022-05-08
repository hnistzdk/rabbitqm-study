package com.zdk.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author zdk
 * @date 2022/5/3 20:17
 */
public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明 Q1 队列与绑定关系
        String queueName = "Q1";
        //声明
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息........... ");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" 接收队列:" + queueName + " 绑定键:" + delivery.getEnvelope().getRoutingKey() + ",消息:" + message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
