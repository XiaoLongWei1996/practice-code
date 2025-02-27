package com.xlw.test.jsr303_demo.annotation;

/**
 * @description: 不兼容字段，两字段必须一个为空一个不为空
 * @Title: Relatively
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.annotation
 * @Date 2024/11/16 15:52
 */
public @interface Relatively {

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
