package com.xlw.test.redis_cache_test.util;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: rabbitmq工具类
 * @Title: RbbitMQUtil
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.util
 * @Date 2024/1/30 16:43
 */
@Slf4j
@Component
public class RabbitMQUtil {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange-name}")
    private String exchange;

    @Value("${mq.routing-key}")
    private String routingKey;

    @Value("${mq.ttl-exchange-name}")
    private String ttlExchange;

    @Value("${mq.ttl-routing-key}")
    private String ttlRoutingKey;

    public void sendMsg(String msg) {
        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
    }

    public void sendMsg(String msg, String exchangeName, String routingKey) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }

    public void safeSend(UserTicket userTicket) {
        String id = String.format("u_order:%s:%s", userTicket.getUserId(), userTicket.getTicketId());
        rabbitTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSONString(userTicket, JSONWriter.Feature.WriteLongAsString),
                message -> {
                    //设置消息属性
                    MessageProperties messageProperties = message.getMessageProperties();
                    //设置消息持久化
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                }
                , new CorrelationData(id));
    }

    public void ttlSend(String msg, int delay) {
        rabbitTemplate.convertAndSend("tex", "ttl", msg,
                message -> {
                    MessageProperties messageProperties = message.getMessageProperties();
//                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    messageProperties.setContentLength(msg.length());
                    messageProperties.setDelay(delay);
                    return message;
                }
                , new CorrelationData(RandomUtil.randomString(10))
        );
    }


}
