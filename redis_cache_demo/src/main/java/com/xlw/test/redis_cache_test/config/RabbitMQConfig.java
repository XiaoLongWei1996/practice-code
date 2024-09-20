package com.xlw.test.redis_cache_test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: rabbitmq配置类
 * @Title: RabbitMQConfig
 * @Author xlw
 * @Package com.xlw.test.rabbitmq_demo.config
 * @Date 2024/1/29 18:47
 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    /**
     * rabbitmq模板
     *
     * @param connectionFactory 连接工厂
     * @return {@link RabbitTemplate}
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //配置发布确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info(correlationData + "：消息发送成功");
            } else {
                log.error(correlationData + "：消息发送失败，原因：" + cause);
            }
        });
//        //配置消息回退
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setReturnsCallback((returned) -> {
//            log.warn("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",returned.getMessage().toString(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
//        });
        //配置编码
        rabbitTemplate.setEncoding("UTF-8");
        //配置序列化和反序列化
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        return rabbitTemplate;
    }

}
