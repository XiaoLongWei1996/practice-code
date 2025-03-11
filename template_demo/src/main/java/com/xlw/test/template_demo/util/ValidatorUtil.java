package com.xlw.test.template_demo.util;

import cn.hutool.core.util.RandomUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: jsr303数据校验工具类
 * @Title: ValidatorUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/8/26 13:50
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

    public static <T> Set<String> beanValidate(T t, Class<?>... groups) {
        //初始化检查器
        Set<String> magSet = new HashSet<>();
        Set<ConstraintViolation<T>> set = validator.validate(t, groups);
        if (!set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                String message = cv.getMessage();
                magSet.add(message);
            }
        }
        return magSet;
    }

    /**
     * 验证
     *
     * @param t t
     */
    public static <T> void validate(T t) {
        validate(t, Default.class);
    }

    public static <T> void validateList(List<T> list) {
        for (T t : list) {
            validate(t);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String s = RandomUtil.randomNumbers(6);
            System.out.println(s);
        }
    }
}
