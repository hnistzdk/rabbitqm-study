package com.zdk.ack;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zdk.utils.RabbitMQUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author zdk
 * @date 2022/5/1 20:33
 */
public class ConsumerAck {

    private static final String QUEUE_NAME = "hello_world";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("work1等待接收消息(处理时间短).........");
//        System.out.println("work2等待接收消息(处理时间长).........");

        //推送来的消息如何被消费的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
//                TimeUnit.SECONDS.sleep(1);
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String msg = new String(message.getBody());
            System.out.println("work1收到消息："+msg);
//            System.out.println("work2收到消息："+msg);
            //multiple表示是否批量确认,类似于tcp的确认,认为tag之前的tag都完成了消费,把它们都一起确认了
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> System.out.println(consumerTag+"消息消费被中断");

        //最多只能允许的未确认消息的数量 如果未确认消息大于等于这个数量 将不会被分发到消息
        //消息会被交给其他未达到perfetchCount值的信道处理
        channel.basicQos(3); //时间短的work1
//        channel.basicQos(2);

        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
