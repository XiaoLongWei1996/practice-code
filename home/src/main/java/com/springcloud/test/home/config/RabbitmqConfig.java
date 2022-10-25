package com.springcloud.test.home.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 肖龙威
 * @date 2022/10/25 15:24
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setExchange("notice_ex");
        return template;
    }

    @Bean
    public Exchange directEx() {
        return new DirectExchange("notice_ex", false, false, null);
    }

    @Bean
    public Queue msgQueue() {
        return new Queue("msg_q", false, false, false, null);
    }

    @Bean
    public Binding binding(Queue msgQueue, Exchange directEx) {
        return BindingBuilder.bind(msgQueue).to(directEx).with("msg").noargs();
    }
}
