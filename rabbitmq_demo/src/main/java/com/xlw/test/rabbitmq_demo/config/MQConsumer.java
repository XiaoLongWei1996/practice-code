package com.xlw.test.rabbitmq_demo.config;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;
import com.xlw.test.rabbitmq_demo.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description: 消息队列消费者
 * @Title: MQConsumer
 * @Author xlw
 * @Package com.xlw.test.rabbitmq_demo.config
 * @Date 2024/1/29 19:44
 */
@Slf4j
@Component
public class MQConsumer {

    @Resource
    private TicketService ticketService;

    private ThreadPoolTaskExecutor threadPool;

    @PostConstruct
    public void init() {
        log.info("初始化线程池");
        threadPool = new ThreadPoolTaskExecutor();
        threadPool.initialize();
        threadPool.setCorePoolSize(4);
        threadPool.setMaxPoolSize(8);
        threadPool.setThreadGroup(new ThreadGroup("order"));
        threadPool.setThreadNamePrefix("o_");
        threadPool.setQueueCapacity(100);
    }

    @RabbitListener(queues = "${mq.queue-name}")
    public void orderConsume(String msg, Channel channel, Message message) {
        threadPool.execute(() -> {
            log.info("消费者消费：{}", msg);
            UserTicket userTicket = JSONObject.parseObject(msg, UserTicket.class);
            try {
                boolean b = ticketService.createOrder(userTicket);
                if (b) {
                    //手动签收
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return;
                }
            } catch (Exception e) {
                try {
                    //拒绝消息，进入死信队列
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
            try {
                //拒绝消息，进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @RabbitListener(queues = "${mq.dead-queue-name}")
    public void deadConsume(String sendMessage, Channel channel, Message message) {
        try {
            log.info("死信消费者消费：{}", sendMessage);
            //手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.warn("死信消费者签收失败：{}", sendMessage);
            try {
                //拒绝消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
