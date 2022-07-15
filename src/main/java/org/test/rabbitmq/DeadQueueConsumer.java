package org.test.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * 死信队列的数据也可以被消费
 * 死信队列消费者
 * @author 肖龙威
 * @date 2022/07/15 14:55
 */
public class DeadQueueConsumer {

    //死信队列名称
    private static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();

        channel.basicConsume(DEAD_QUEUE_NAME, true
                , ((consumerTag, message) -> {
                    String s = new String(message.getBody());
                    System.out.println(s);
                }), consumerTag -> {});
    }
}
