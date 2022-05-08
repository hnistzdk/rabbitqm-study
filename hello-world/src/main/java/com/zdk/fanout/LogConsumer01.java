package com.zdk.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

/**
 * @author zdk
 * @date 2022/5/3 18:44
 */
public class LogConsumer01 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //创建队列
        String queueName = channel.queueDeclare().getQueue();

        //将队列绑定到生产者生成的交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("消费者1等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag,message)-> System.out.println("消费者1收到消息："+new String(message.getBody()));

        //进行消费
        channel.basicConsume(queueName, true, deliverCallback, (consumerTag)->{});

    }
}
