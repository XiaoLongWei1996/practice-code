package com.xlw.test.template_demo.config.annotation;

/**
 * @description: 级联字段，同时为空或者不为空
 * @Title: Cascade
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.annotation
 * @Date 2024/11/16 15:47
 */
public @interface Cascade {

    /**
     * 字段 1
     *
     * @return {@link String }
     */
    String field1();

    /**
     * 字段 2
     *
     * @return {@link String }
     */
    String field2();
}
