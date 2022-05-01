package com.zdk.workQueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

/**
 * @author zdk
 * @date 2022/5/1 20:00
 */
public class Work {

    private static final String QUEUE_NAME = "hello_world";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("work2等待接收消息.........");

        //推送来的消息如何被消费的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println(msg);
        };

        CancelCallback cancelCallback = (consumerTag) -> System.out.println(consumerTag+"消息消费被中断");

        channel.basicConsume(QUEUE_NAME, true,deliverCallback,cancelCallback);
    }
}
