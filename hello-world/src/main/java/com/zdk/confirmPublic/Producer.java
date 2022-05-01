package com.zdk.confirmPublic;

import com.rabbitmq.client.Channel;
import com.zdk.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author zdk
 * @date 2022/5/1 21:40
 */
public class Producer {

    private static final int MESSAGE_NUM = 5000;

    public static void main(String[] args) throws Exception {
//        publicSingleMessageConfirm();
        publicMultipleMessageConfirm();
    }

    /**
     * 单个消息发布确认
     * @throws Exception
     */
    public static void publicSingleMessageConfirm() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        //生成一个队列
        channel.queueDeclare(queueName,false,false, false, null);

        //开启发布确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_NUM; i++) {
            channel.basicPublish("", queueName, null, (i+"").getBytes(StandardCharsets.UTF_8));
            //单个消息 马上进行发布确认
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发送"+MESSAGE_NUM+"条消息单个确认耗时："+(end-start)+"ms");
    }

    /**
     * 批量消息发布确认
     * @throws Exception
     */
    public static void publicMultipleMessageConfirm() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        //生成一个队列
        channel.queueDeclare(queueName,false,false, false, null);

        //开启发布确认
        channel.confirmSelect();
        //设置确认消息大小
        int batchSize = 200;
        //未确认消息大小
        int outstandingMessageCount = 0;

        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_NUM; i++) {
            channel.basicPublish("", queueName, null, (i+"").getBytes(StandardCharsets.UTF_8));
            outstandingMessageCount++;
            //到达设置的大小后统一确认
            if (outstandingMessageCount == batchSize) {
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }
        //为了确保还有剩余没有确认消息 再次确认
        if (outstandingMessageCount > 0) {
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发送"+MESSAGE_NUM+"条消息批量确认耗时："+(end-start)+"ms");
    }
}
