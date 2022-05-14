package com.zdk.rabbitmqspringboot.consumer;

import com.zdk.rabbitmqspringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zdk
 * @date 2022/5/14 17:00
 */
@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("从队列{} 接收到消息:{}",ConfirmConfig.CONFIRM_QUEUE_NAME,msg);
    }

    /**
     * 告警队列接收备份消息
     * @param message
     */
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMessage(Message message){
        String msg = new String(message.getBody());
        log.warn("告警队列{} 接收到消息:{}",ConfirmConfig.WARNING_QUEUE_NAME,msg);
    }
}
