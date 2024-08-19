package com.xlw.test.jsr303_demo.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @description: 手动校验
 * @Title: ValidatorUtil
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.util
 * @Date 2024/8/19 13:40
 */
public class ValidatorUtil {


    /**
     * 验证器
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 验证
     *
     * @param t      t
     * @param groups 组
     */
    public static <T> void validate(T t, Class<?>... groups) {
        //初始化检查器
        Set<ConstraintViolation<T>> set = validator.validate(t, groups);
        if (!set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                String property = cv.getPropertyPath().toString();
                String message = cv.getMessage();
                throw new RuntimeException(property + message);
            }
        }
    }

    /**
     * 验证
     *
     * @param t t
     */
    public static <T> void validate(T t) {
        validate(t, Default.class);
    }
}
