package com.xlw.test.template_demo.config.annotation;

import com.piaoshui.ncp.annotation.validator.RelatedConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @description: 关联字段检测
 * @Title: Related
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.annotation
 * @Date 2024/11/16 10:45
 */
@Documented
//指定校验器
@Constraint(
        validatedBy = {RelatedConstraintValidator.class}
)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Related {

    /**
     * 消息
     *
     * @return {@link String }
     */
    String message() default "";

    /**
     * 组
     *
     * @return {@link Class }<{@link ? }>{@link [] }
     */
    Class<?>[] groups() default {};

    /**
     * 相关字段，两字段必须同时为空或者不为空
     *
     * @return {@link String[] }
     */
    Cascade[] cascades() default {};

    /**
     * 不兼容字段，两字段必须一个为空一个不为空
     *
     * @return {@link String[] }
     */
    Relatively[] relativelys() default {};

    /**
     * 任意一个字段不为空
     *
     * @return {@link String[] }
     */
    String[] anyOneNotNullFields() default {};

    /**
     * 有效载荷
     *
     * @return {@link Class }<{@link ? } {@link extends } {@link Payload }>{@link [] }
     */
    Class<? extends Payload>[] payload() default {};
}
