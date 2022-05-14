package com.zdk.rabbitmqspringboot.consumer;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import com.zdk.rabbitmqspringboot.config.DelayedQueueConfig;
import com.zdk.rabbitmqspringboot.constant.TtlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zdk
 * @date 2022/5/13 21:16
 */
@Slf4j
@Component
public class DeadLetterConsumer {

    /**
     * 死信队列监听
     * @param message
     * @param channel
     */
    @RabbitListener(queues = TtlConstant.DEAD_QUEUE)
    public void receive(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列信息{}", DateUtil.now(), msg);
    }

    /**
     * 延时队列监听
     * @param message
     * @param channel
     */
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void delayReceive(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延迟队列的消息:{}", DateUtil.now(), msg);
    }
}
