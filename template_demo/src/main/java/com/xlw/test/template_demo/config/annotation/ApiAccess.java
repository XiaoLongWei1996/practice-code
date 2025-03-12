package com.xlw.test.template_demo.config.annotation;

import java.lang.annotation.*;

/**
 * @description: api访问
 * @Title: ApiAccess
 * @Author xlw
 * @Package com.invoice.tcc.cons
 * @Date 2024/8/12 16:02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiAccess {
    boolean value() default false;
}
