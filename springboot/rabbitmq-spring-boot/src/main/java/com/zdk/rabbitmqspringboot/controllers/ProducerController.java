package com.zdk.rabbitmqspringboot.controllers;

import com.zdk.rabbitmqspringboot.callback.MessageCallback;
import com.zdk.rabbitmqspringboot.config.ConfirmConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author zdk
 * @date 2022/5/14 16:55
 */
@Api("发布确认 消息发送接口")
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageCallback messageCallback;

    /**
     * 这里需要对MessageCallback进行注入
     * 因为它实现的是RabbitTemplate的内部接口,如果不将它注入到RabbitTemplate中,
     * 在调用的时候，RabbitTemplate就找不到对应的MessageCallback实例
     *
     * 这个@PostConstruct注解所标注的方法,会在Bean初始化以后(完成属性填充之后 populate)执行
     * 因为我们需要在ProducerController类的RabbitTemplate和MessageCallback属性都被注入后
     * 再将MessageCallback注入到RabbitTemplate中
     *
     * 执行顺序是：
     * 1.构造方法Constructor
     * 2.@Autowired
     * 3.@PostConstruct
     *
     * 当然 还可以让当前类实现implements InitializingBean 接口,重写afterPropertiesSet方法
     * 在这个方法中调用init方法也可以
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(messageCallback);
        /*
          true：交换机无法将消息进行路由时，会将该消息返回给生产者
          false：如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnsCallback(messageCallback);
    }

    @GetMapping("/sendMessage/{message}")
    public void  sendMessage(@PathVariable String message){
//        CorrelationData correlationData1 = new CorrelationData("1");
//        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData1);
//        log.info(ConfirmConfig.CONFIRM_ROUTING_KEY+"发送消息内容为:{}",message);

//        //不存在的交换机
//        CorrelationData correlationData2 = new CorrelationData("2");
//        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME+"22",ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData2);
//        log.info(ConfirmConfig.CONFIRM_EXCHANGE_NAME+"22"+"发送消息内容为:{}",message);

        //不存在与之对应的队列的routingKey
        CorrelationData correlationData3 = new CorrelationData("11");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY+"22",message,correlationData3);
        log.info(ConfirmConfig.CONFIRM_EXCHANGE_NAME+"发送消息内容为:{}",message);
    }
}
