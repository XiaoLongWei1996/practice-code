package com.springcloud.test.home.consumer;

import entity.GloableMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author 肖龙威
 * @date 2022/10/25 15:23
 */
@Component
@RabbitListener(queues = "msg_q")
public class MessageConsumer {

    @RabbitHandler
    public void receive(@Payload GloableMessage message) {
        System.out.println(message);
    }

}
