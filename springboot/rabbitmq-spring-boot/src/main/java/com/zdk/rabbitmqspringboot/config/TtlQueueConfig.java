package com.zdk.rabbitmqspringboot.config;

import com.zdk.rabbitmqspringboot.constant.TtlConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zdk
 * @date 2022/5/13 20:06
 * 1.一个生产者,向普通交换机投递消息
 * 2.一个direct类型的普通交换机,它绑定两个普通队列,一个消息ttl为10s,一个为40s
 * 3.然后再来一个type为direct的死信交换机,它绑定一个普通队列,
 * 用于接收从上面两个普通队列消息过期后来的死信,
 * 4.再来一个死信消费者
 * 5.注意 两个普通队列与死信队列 这三个队列与死信交换机的routingKey应该是一样的
 * 否则普通队列中过期的消息路由不到死信队列中
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 声明普通交换机
     * @return
     */
    @Bean
    public DirectExchange simpleExchange(){
        return ExchangeBuilder
                .directExchange(TtlConstant.SIMPLE_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 声明死信交换机
     * @return
     */
    @Bean
    public DirectExchange deadExchange(){
        return ExchangeBuilder
                .directExchange(TtlConstant.DEAD_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 普通队列1 ttl 10s
     * @return
     */
    @Bean
    public Queue simpleQueue01(){
        return  QueueBuilder.durable(TtlConstant.SIMPLE_QUEUE01)
                //指定这个队列的死信交换机
                .deadLetterExchange(TtlConstant.DEAD_EXCHANGE)
                //指定与死信交换机的routingKey
                .deadLetterRoutingKey(TtlConstant.DEAD_EXCHANGE_ROUTING_KEY)
                //设置消息过期时间
                .ttl(10000)
                .build();
    }

    /**
     * 普通队列2 ttl 40s
     * @return
     */
    @Bean
    public Queue simpleQueue02(){
        return  QueueBuilder.durable(TtlConstant.SIMPLE_QUEUE02)
                //绑定这个队列的死信交换机
                .deadLetterExchange(TtlConstant.DEAD_EXCHANGE)
                //绑定与死信交换机的routingKey
                .deadLetterRoutingKey(TtlConstant.DEAD_EXCHANGE_ROUTING_KEY)
                //设置消息过期时间
                .ttl(40000)
                .build();
    }

    /**
     * 死信队列:接收满足routingKey的死信交换机的消息
     * @return
     */
    @Bean
    public Queue deadQueue(){
        return  QueueBuilder.durable(TtlConstant.DEAD_QUEUE).build();
    }

    /**
     * 普通队列1绑定到普通交换机
     * @param simpleQueue01
     * @param simpleExchange
     * @return
     */
    @Bean
    public Binding simpleQueue01BindSimpleExchange(
            @Qualifier("simpleQueue01") Queue simpleQueue01,
            @Qualifier("simpleExchange") DirectExchange simpleExchange
    ){
        return BindingBuilder
                //队列
                .bind(simpleQueue01)
                //到交换机
                .to(simpleExchange)
                //routingKey
                .with(TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_01);
    }

    /**
     * 普通队列2绑定到普通交换机
     * @param simpleQueue02
     * @param simpleExchange
     * @return
     */
    @Bean
    public Binding simpleQueue02BindSimpleExchange(
            @Qualifier("simpleQueue02") Queue simpleQueue02,
            @Qualifier("simpleExchange") DirectExchange simpleExchange
    ){
        return BindingBuilder
                //队列
                .bind(simpleQueue02)
                //到交换机
                .to(simpleExchange)
                //routingKey
                .with(TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_02);
    }

    /**
     * 死信队列绑定到死信交换机
     * @param deadQueue
     * @param deadExchange
     * @return
     */
    @Bean
    public Binding deadQueueBindDeadExchange(
            @Qualifier("deadQueue") Queue deadQueue,
            @Qualifier("deadExchange") DirectExchange deadExchange
    ){
        return BindingBuilder
                //队列
                .bind(deadQueue)
                //到交换机
                .to(deadExchange)
                //routingKey
                .with(TtlConstant.DEAD_EXCHANGE_ROUTING_KEY);
    }

    /**
     * 不设置消息过期时间的普通队列
     * ttl由发送方指定
     * @return
     */
    @Bean
    public Queue noTtlSimpleQueue(){
        return QueueBuilder
                .durable(TtlConstant.NO_TTL_SIMPLE_QUEUE)
                .deadLetterExchange(TtlConstant.DEAD_EXCHANGE)
                .deadLetterRoutingKey(TtlConstant.DEAD_EXCHANGE_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定到普通交换机上
     * @param noTtlSimpleQueue
     * @param simpleExchange
     * @return
     */
    @Bean
    public Binding noTtlSimpleQueueBindSimpleExchange(
            @Qualifier("noTtlSimpleQueue") Queue noTtlSimpleQueue,
            @Qualifier("simpleExchange") DirectExchange simpleExchange
    ){
        return BindingBuilder.bind(noTtlSimpleQueue)
                .to(simpleExchange)
                .with(TtlConstant.SIMPLE_QUEUE_ROUTING_KEY_03);
    }
}

