package com.zdk.deadQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zdk.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author zdk
 * @date 2022/5/7 20:27
 * 生产者
 */
public class Producer {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String NORMAL_ROUTING_KEY = "normal";
    public static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定死信交换机与死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

        /*
        普通队列将消息在三种情况下转到死信队列需要进行以下参数配置
         */
        //为普通队列声明参数
        Map<String, Object> arguments = new HashMap<>();
        //指定它的死信队列
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //指定死信队列与死信交换机的routingKey
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        //设置普通队列的最大消息长度
        arguments.put("x-max-length", 2);

        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //绑定普通交换机与普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);


        System.out.println("生产者准备发送消息......");
        Scanner cin = new Scanner(System.in);

        /*
         * 1.情况一:消息处理超时从而进入死信队列
         * 构建参数 消费者将消息过期时间设置为10秒
         * 如果消息超过10秒没被确认消费，那么就会从普通队列移除 并加入到死信队列中
         */
        AMQP.BasicProperties props =
                new AMQP.BasicProperties()
                .builder()
                .expiration("10000")
                .build();

        /*
         * 1.情况二:普通队列消息达到最大长度，超过的部分加入到死信队列
         * 即不用传递参数
         */
        props = null;

        while (cin.hasNext()) {
            String message = cin.next();
            //发送消息
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, props, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
