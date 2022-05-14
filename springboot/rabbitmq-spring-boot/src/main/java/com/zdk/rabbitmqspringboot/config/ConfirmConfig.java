package com.zdk.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zdk
 * @date 2022/5/14 16:02
 * 发布确认的 配置
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";

    public static final String HAVE_BACKUP_CONFIRM_EXCHANGE_NAME = "have_backup_confirm_exchange";
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    /*@Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .build();
    }*/

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME)
                .build();
    }

    @Bean
    public Binding confirmBinding(
            @Qualifier("confirmQueue") Queue confirmQueue,
            @Qualifier("confirmExchange") DirectExchange confirmExchange
    ) {
        return BindingBuilder
                .bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }

    //=============================以下是备份交换机的配置

    /**
     * 声明一个拥有备份交换机的交换机
     * 并指定它的备份交换机的名字
     * @return
     */
    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //指定它的备份交换机
                .alternate(BACKUP_EXCHANGE_NAME)
                .build();
    }

    /**
     * 声明备份交换机为fanout类型
     * 保证与它绑定的每个交换机都能处理到备份的消息
     * @return
     */
    @Bean
    public FanoutExchange backupExchange(){
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 声明接收备份消息的普通处理队列
     * @return
     */
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME)
                .build();
    }

    /**
     * 声明接收备份消息的告警处理队列
     * @return
     */
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME)
                .build();
    }

    @Bean
    public Binding bindBackupQueue(
            @Qualifier("backupQueue") Queue backupQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange

    ){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding bindWarningQueue(
            @Qualifier("warningQueue") Queue warningQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange

    ){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}
