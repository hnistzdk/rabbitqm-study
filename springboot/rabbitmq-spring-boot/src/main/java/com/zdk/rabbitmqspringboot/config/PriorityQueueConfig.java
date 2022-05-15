package com.zdk.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zdk
 * @date 2022/5/15 11:03
 * 优先消息队列
 * 设置设置时需要注意两个点
 * 1.给消息队列添加优先级
 * 2.给消息添加优先级
 */
@Configuration
public class PriorityQueueConfig {

    public static final String PRIORITY_EXCHANGE_NAME = "priority.exchange";
    public static final String PRIORITY_QUEUE_NAME = "priority.queue";

    @Bean
    public DirectExchange priorityExchange() {
        return ExchangeBuilder.directExchange(PRIORITY_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue priorityQueue(){
        return QueueBuilder.durable(PRIORITY_QUEUE_NAME)
                .maxPriority(10)
                .build();
    }

    @Bean
    public Binding bindPriorityQueue(
            @Qualifier("priorityQueue") Queue priorityQueue,
            @Qualifier("priorityExchange") DirectExchange priorityExchange
    ){
        return BindingBuilder.bind(priorityQueue).to(priorityExchange)
                    .with("priority");
    }

    @Bean
    public Queue lazyQueue(){
        return QueueBuilder.durable("lazy.queue")
                .lazy()
                .build();
    }
}
