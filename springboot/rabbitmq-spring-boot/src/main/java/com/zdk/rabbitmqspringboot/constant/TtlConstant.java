package com.zdk.rabbitmqspringboot.constant;

/**
 * @author zdk
 * @date 2022/5/13 20:12
 */
public class TtlConstant {
    /**
     * 普通交换机名称
     */
    public static final String SIMPLE_EXCHANGE = "simple_exchange";
    /**
     * 死信交换机名称
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";

    /**
     * 普通队列1名称 ttl:10s
     */
    public static final String SIMPLE_QUEUE01 = "simple_queue10";
    /**
     * 普通队列2名称 ttl:40s
     */
    public static final String SIMPLE_QUEUE02 = "simple_queue40";

    /**
     * 死信队列名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    /**
     * 普通队列1与普通交换机的routingKey
     */
    public static final String SIMPLE_QUEUE_ROUTING_KEY_01 = "simple_queue_routing_key_01";

    /**
     * 普通队列2与普通交换机的routingKey
     */
    public static final String SIMPLE_QUEUE_ROUTING_KEY_02 = "simple_queue_routing_key_02";

    /**
     * 普通队列3与普通交换机的routingKey
     */
    public static final String SIMPLE_QUEUE_ROUTING_KEY_03 = "simple_queue_routing_key_03";

    /**
     * 两个普通队列和死信队列与死信交换机的routingKey
     */
    public static final String DEAD_EXCHANGE_ROUTING_KEY = "dead_exchange_routing_key";

    /**
     * 一个不设置过期时间的普通队列 ttl由生产者指定
     */
    public static final String NO_TTL_SIMPLE_QUEUE = "no_ttl_simple_queue";
}
