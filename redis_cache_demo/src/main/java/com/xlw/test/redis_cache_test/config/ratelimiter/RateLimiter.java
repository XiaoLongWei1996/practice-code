package com.xlw.test.redis_cache_test.config.ratelimiter;

import java.lang.annotation.*;

/**
 * @description: 限流
 * @Title: RateLimiter
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.ratelimiter
 * @Date 2024/1/31 14:38
 */
@Target(ElementType.METHOD)          // 定义注解的作用目标
@Retention(RetentionPolicy.RUNTIME) //运行期注解，可以在运行时获取注解信息并处理
@Documented
public @interface RateLimiter {

    String name();

    int limit() default 10;

    int timeout() default 1000;

}
