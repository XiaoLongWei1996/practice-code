package com.xlw.test.redis_cache_test.config;

import com.xlw.test.redis_cache_test.config.listener.RedisKeyExpireListener;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @description: redis配置类
 * @Title: RedisConfig
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config
 * @Date 2023/12/23 19:39
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建模板
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer =
                new GenericJackson2JsonRedisSerializer();
        // key和 hashKey采用 string序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // value和 hashValue采用 JSON序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        return listenerContainer;
    }

    @Bean
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        return new RedisKeyExpireListener(listenerContainer);
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config
                .useSingleServer()
                .setAddress("redis://192.168.86.128:6379")
                .setDatabase(0)
                .setPassword("123456")
                .setConnectionPoolSize(4)
                .setConnectionMinimumIdleSize(2)
                .setConnectTimeout(5000);
        return Redisson.create(config);
    }


}
