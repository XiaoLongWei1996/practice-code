package com.xlw.test.template_demo.config.lock;

import javax.validation.Constraint;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: 分布式锁
 * @Title: Lock
 * @Author xlw
 * @Package com.xlw.test.template_demo.config.lock
 * @Date 2024/12/19 15:02
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 锁名称
     *
     * @return {@link String }
     */
    String name();

    /**
     * 超时
     *
     * @return long
     */
    long timeout() default 3000L;

    /**
     * 单位,默认毫秒
     *
     * @return {@link TimeUnit }
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
