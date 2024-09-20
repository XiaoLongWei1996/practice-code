package com.xlw.test.redis_cache_test.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @description: redis key过期监听
 * @Title: RedisKeyExpireListener
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.listener
 * @Date 2024/2/1 16:17
 */
@Slf4j
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("redis key过期事件监听到：{}", message.toString());
    }
}
