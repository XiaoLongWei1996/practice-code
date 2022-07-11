package org.test.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 消息应答机制:消费者在消费完一条消息,发送一条ack并告诉mq,然后mq删除这条数据
 * 自动应答:消息发送给消费者,立马会发送一条ack给mq,mq会把该消息标记为删除状态,此时其它消费者无法再消费;如果消费者来不及消费消息,但是mq还一直给
 * 它推送消息容易造成消息堆积使消费者内存耗尽;当消费者还没消费完消息宕机,那么会导致消息丢失。
 *
 * 手动应答:当消费者彻底消费完一条数据后再发送给mq一条ack;当消费者消费消息的时候宕机,但是没发送ack,那么该消息还可以被其它消费者消费,确保了消息
 * 不会丢失。
 * @author 肖龙威
 * @date 2022/07/11 14:59
 */
public class AckConsumer {

    private static final String QUEUE_NAME = "test";

    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //成功消费回调对象
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
            channel.basicAck(
                    delivery.getEnvelope().getDeliveryTag(), //消息的标签(相当于消息的id号)
                    false  //是否批量应答(true:消费者收到1,2,3,4条信息,如果当前正在执行4被确认应答,那么1,2,3也会被确认应答;
                           //false:不批量应答,只应答当前的数据 )
            );
        };
        //失败消费
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费失败");
        };
        //手动应答
        Boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
