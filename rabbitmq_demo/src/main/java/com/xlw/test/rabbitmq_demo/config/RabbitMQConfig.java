package com.xlw.test.rabbitmq_demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @Resource
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @Resource
    private RabbitMQProperties rabbitMQProperties;
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
                log.info(correlationData.getReturned().getMessage().toString() + "：消息发送成功");
            } else {
                log.error(correlationData + "：消息发送失败，原因：" + cause);
            }
        });
        //配置消息回退
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback((returned) -> {
            log.warn("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",returned.getMessage().toString(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
        });
        //配置编码
        rabbitTemplate.setEncoding("UTF-8");
        //配置序列化和反序列化
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        rabbitListenerContainerFactory.setMessageConverter(new SimpleMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMQProperties.getExchangeName(), true, false);
    }

    @Bean
    public Queue queue() {
        Map<String, Object> arguments = new HashMap<>();
        //设置为惰性队列
        arguments.put("x-queue-mode", "lazy");
        //添加死信交换机
        arguments.put("x-dead-letter-exchange", rabbitMQProperties.getDeadExchangeName());
        //添加死信路由
        arguments.put("x-dead-letter-routing-key", rabbitMQProperties.getDeadRoutingKey());
        return new Queue(rabbitMQProperties.getQueueName(), true, false, false, arguments);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(rabbitMQProperties.getRoutingKey());
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(rabbitMQProperties.getDeadExchangeName(), true, false);
    }

    @Bean
    public Queue deadQueue() {
        return new Queue(rabbitMQProperties.getDeadQueueName(), true, false, false);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(rabbitMQProperties.getDeadRoutingKey());
    }



}
