package com.xlw.test.rabbitmq_demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 消息
 * @Title: Message
 * @Author xlw
 * @Package com.xlw.test.rabbitmq_demo.config
 * @Date 2024/1/29 19:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessage<T> {

    private String id;

    private T data;

    private Long timestamp;

    public static <T> SendMessage<T> of(String id, T data) {
        return new SendMessage<>(null, data, System.currentTimeMillis());
    }

}
