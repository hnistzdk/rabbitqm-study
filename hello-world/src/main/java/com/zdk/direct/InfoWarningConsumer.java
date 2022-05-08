package com.zdk.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

/**
 * @author zdk
 * @date 2022/5/3 19:23
 */
public class InfoWarningConsumer {
    public static final String EXCHANGE_NAME = "logs2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //创建队列
        String queueName = channel.queueDeclare().getQueue();

        //给这个队列同时绑定两个routingKey info和warning
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");

        System.out.println("消费者等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, message)-> {
            String routingKey = message.getEnvelope().getRoutingKey();
            System.out.println("routingKey:"+routingKey+"收到消息："+new String(message.getBody()));
        };

        //进行消费
        channel.basicConsume(queueName, true, deliverCallback, (consumerTag)->{});

    }
}
