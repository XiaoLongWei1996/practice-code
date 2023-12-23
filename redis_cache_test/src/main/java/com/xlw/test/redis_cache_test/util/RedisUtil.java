package com.xlw.test.redis_cache_test.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis操作工具类
 * @Title: RedisUtil
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.util
 * @Date 2023/12/23 19:38
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*-------------------------------------------------------key操作-------------------------------------------------------------*/

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /*---------------------------------------------------String操作--------------------------------------------------------------*/

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object getSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    public long length(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
}
