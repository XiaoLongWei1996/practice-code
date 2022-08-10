package com.test.springboot.myannotation;

import com.test.springboot.myenum.Status;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 肖龙威
 * @date 2022/07/29 8:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Confirm {

    String value() default "";

    Class clazz();

    Status status();
}
