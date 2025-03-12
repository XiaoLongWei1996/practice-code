package com.xlw.test.template_demo.config.annotation.validator;

/**
 * @description: 相对的, 两字段不能同时为空或者不为空
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
