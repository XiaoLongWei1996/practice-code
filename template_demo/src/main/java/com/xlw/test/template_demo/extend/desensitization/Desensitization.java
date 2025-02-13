package com.xlw.test.template_demo.extend.desensitization;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 脱敏注解
 * @Title: Desensitization
 * @Author xlw
 * @Package com.invoice.tcc.config.annotations
 * @Date 2024/10/30 17:49
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerialize.class)
public @interface Desensitization {

    DesensitizationTypeEnum type() default DesensitizationTypeEnum.CUSTOM_RULE;

    /**
     * 开始包含
     *
     * @return int
     */
    int startInclude();
    
    /**
     * end include
     *
     * @return int
     */
    int endInclude();
}
