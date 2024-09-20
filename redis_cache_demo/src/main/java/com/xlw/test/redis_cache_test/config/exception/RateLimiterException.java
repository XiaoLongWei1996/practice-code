package com.xlw.test.redis_cache_test.config.exception;

/**
 * @description: 限流异常
 * @Title: RateLimiterException
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.exception
 * @Date 2024/1/31 15:36
 */
public class RateLimiterException extends RuntimeException {

    public RateLimiterException(String message) {
        super(message);
    }
}
