package com.xlw.test.redis_cache_test.config.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: 查询缓存
 * @Title: Cache
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.cache
 * @Date 2024/1/6 17:42
 */
@Target(ElementType.METHOD) // 定义注解的作用目标
@Retention(RetentionPolicy.RUNTIME) //运行期注解，可以在运行时获取注解信息并处理
@Documented // 表示该注解会出现在javadoc文档中
public @interface Cache {

    /**
     * 缓存前缀
     * @return
     */
    String prefix() default "";

    /**
     * 缓存key
     * @return
     */
    String key();

    /**
     * 缓存时间
     * @return
     */
    long timeout() default 1000;

    /**
     * 缓存时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
