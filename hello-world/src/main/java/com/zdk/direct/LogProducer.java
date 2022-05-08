package com.zdk.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zdk.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/**
 * @author zdk
 * @date 2022/5/3 19:21
 */
public class LogProducer {
    public static final String EXCHANGE_NAME = "logs2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner cin = new Scanner(System.in);

        Random random = new Random();
        while (cin.hasNext()){
            String message = cin.next();
            String[] routingKeys = {"info","warning","error"};
            String routingKey = routingKeys[random.nextInt(3)];
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出"+routingKey+"级别的消息："+message);
        }
    }
}
