package org.test.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 延迟队列:队列里面的消息在过期时间内没有被消费,那么会被丢弃获取加入到死信队列里面;
 * 设置消息的TTL的两种方式:
 *      1.生产者生成消息的时候给消息加上过期时间;
 *      2.队列自己指定消息的过期时间.
 * 如果以上两种情况一起使用,那么较小的那个值将会被使用;
 * 两者的区别:
 *      生产者自己设置的消息过期时间,即使消息过期了,也不会立马被移除,而mq在把该消息投递给消费者前判断是否过期丢弃;
 *      队列设置的过期时间,只要消息的过期时间一到,立马会被移除
 * @author 肖龙威
 * @date 2022/07/15 16:07
 */
public class TTLQueue {

    //一般交换机名称
    private static final String EXCHANGE_NAME = "log";

    private static final String QUEUE_NAME = "queue";

    //死信交换机名称
    private static final String DEAD_EXCHANGE_NAME = "dead_log";

    //死信队列名称
    private static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //创建队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "dead");

        Map<String, Object> params = new HashMap<>();
        //指定死信队列交换机
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        //指定死信队列路由
        params.put("x-dead-letter-routing-key", "dead");
        //指定队列大小
        params.put("x-max-length", 10);
        //指定队列的消息过期时间
        params.put("x-message-ttl", 5000);
        channel.queueDeclare(QUEUE_NAME, false, false, false, params);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "log");
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration("1000").build();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            //设置消息的过期时间,消息过期后会入死信队列
            channel.basicPublish(EXCHANGE_NAME, "log", null, message.getBytes());
        }

    }
}
