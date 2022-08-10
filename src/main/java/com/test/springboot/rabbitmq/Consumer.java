package com.test.springboot.rabbitmq;

import com.rabbitmq.client.Channel;
import com.test.springboot.domain.MyMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

/**
 * @author 肖龙威
 * @date 2022/07/18 16:27
 */
//@Component
public class Consumer {


//    @RabbitListener(queues = "qu-delayed")
//    public void received(Message message, Channel channel) throws IOException {
//        System.out.println("消费:" + new String(message.getBody()));
//        //手动签收
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }

    @RabbitListener(queues = "qu-delayed", containerFactory = "myRabbitListenerContainerFactory")
    public void received01(@Payload MyMessage data, Message message, Channel channel) throws IOException {
        System.out.println(data);
        //手动签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = "qu-back")
    public void backReceived(Message message, Channel channel) {
        System.out.println("备份队列消费:" + new String(message.getBody()));
    }

}
