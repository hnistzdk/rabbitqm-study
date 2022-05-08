package com.zdk.deadQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

/**
 * @author zdk
 * @date 2022/5/7 20:27
 * 死信队列消费者
 */
public class DeadQueueConsumer {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println("死信队列消费者消费了消息："+msg);
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        System.out.println("死信队列消费者准备接收消息......");

        channel.basicConsume(DEAD_QUEUE, false, deliverCallback, consumerTag -> {});
    }
}
