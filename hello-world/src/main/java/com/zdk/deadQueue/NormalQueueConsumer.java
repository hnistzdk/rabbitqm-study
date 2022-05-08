package com.zdk.deadQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

/**
 * @author zdk
 * @date 2022/5/7 20:26
 * 普通队列消费者
 */
public class NormalQueueConsumer {

    public static final String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if (msg.equals("garbage")){
                System.out.println("普通队列消费者拒收了消息："+msg);
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }else{
                System.out.println("普通队列消费者消费了消息："+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        System.out.println("普通队列消费者准备接收消息......");


        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {});

    }
}
