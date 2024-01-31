package com.xlw.test.rabbitmq_demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 兔子mqproperties
 *
 * @description: rabbitmq属性配置类
 * @Title: RabbitMQProperties
 * @Author xlw
 * @Package com.xlw.test.rabbitmq_demo.config
 * @Date 2024/1/29 19:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "mq")
public class RabbitMQProperties {

    /**
     * 交换名字
     */
    private String exchangeName;

    /**
     * 路由关键
     */
    private String routingKey;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 死信队列交换名
     */
    private String deadExchangeName;

    /**
     * 死信队列由键
     */
    private String deadRoutingKey;

    /**
     * 死信队列名称
     */
    private String deadQueueName;

    /**
     * TTL交换名称
     */
    private String ttlExchangeName;

    /**
     * TTL路由密钥
     */
    private String ttlRoutingKey;

    /**
     * TTL队列名称
     */
    private String ttlQueueName;

}
