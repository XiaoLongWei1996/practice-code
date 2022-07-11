package org.test.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 公平分发:mq会轮询的把队列消息分发给消费者,但是消费者有的处理快,有的处理慢,如果公平分发会导致处理快的消费者
 * 一直处于空闲状态,消费慢的一直处于忙碌状态;
 * 不公平分发:消费者处理完消息,并发送ack给mq后才会,mq才会分发新消息给消费者,这样性能好的消费者可以消费更多的
 * 消息,但是如果消费者都没有完成手头上的消息,而生产者还一直再生产,可能会把队列撑破,这时只能添加新的消费者或者
 * 改变储存任务的策略。
 * @author 肖龙威
 * @date 2022/07/11 16:48
 */
public class UnfairConsumer {

    private static final String QUEUE_NAME = "durable_test";

    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //不公平分发
        int prefetch = 1;
        channel.basicQos(prefetch);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费失败");
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
