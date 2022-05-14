package com.zdk.rabbitmqspringboot.controllers;

import cn.hutool.core.date.DateUtil;
import com.zdk.rabbitmqspringboot.config.DelayedQueueConfig;
import com.zdk.rabbitmqspringboot.constant.TtlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zdk
 * @date 2022/5/13 20:45
 */
@Api(value = "消息发送方")
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "发送消息")
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", DateUtil.now(), message);
        rabbitTemplate.convertAndSend(TtlConstant.SIMPLE_EXCHANGE, TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_01, "消息来自 ttl 为 10S 的队列: "+message);
        rabbitTemplate.convertAndSend(TtlConstant.SIMPLE_EXCHANGE, TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_02, "消息来自 ttl 为 40S 的队列: "+message);
    }

    @ApiOperation(value = "发送指定过期时间的消息")
    @GetMapping("/sendTtlMsg/{message}/{ttl}")
    public void sendTtlMsg(@PathVariable String message, @PathVariable String ttl){
        log.info("当前时间：{},发送一条指定过期时间的信息给普通队列:{}", DateUtil.now(), message);
        rabbitTemplate.convertAndSend(TtlConstant.SIMPLE_EXCHANGE, TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_03, message,correlationData->{
            //发送方设置消息过期时间
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        }
        );
    }

    @ApiOperation(value = "发送指定时间的消息到延迟队列")
    @GetMapping("/sendMsg2DelayedExchange/{message}/{ttl}")
    public void sendMsg2DelayedExchange(@PathVariable String message, @PathVariable Integer ttl){
        log.info("当前时间：{},发送一条时长为 {} 的消息给延迟队列: {}", DateUtil.now(),ttl, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg->{
                    //发送方设置消息延迟时间
                    msg.getMessageProperties().setDelay(ttl);
                    return msg;
                }
        );
    }
}
