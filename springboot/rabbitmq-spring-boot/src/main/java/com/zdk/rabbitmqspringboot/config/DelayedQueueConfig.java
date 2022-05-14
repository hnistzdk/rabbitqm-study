package com.zdk.rabbitmqspringboot.config;

import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zdk
 * @date 2022/5/14 14:55
 * 使用插件实现延时队列
 */
@Configuration
public class DelayedQueueConfig {

    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY = "delayed_routing_key";

    /**
     * 声明自定义交换机
     * @return
     */
    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> args = new HashMap<>();
        //指定延时交换机的类型 为direct
        args.put("x-delayed-type", BuiltinExchangeType.DIRECT.getType());
        //构造函数的参数里的type 是声明这个交换机的功能的类型 是一个延迟消息的类型
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,args);
    }

    /**
     * 接收延时消息的队列
     * @return
     */
    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    /**
     * 绑定延时交换机和队列
     * @param delayedQueue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding bindingDelayed(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange
    ){
        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }

}
