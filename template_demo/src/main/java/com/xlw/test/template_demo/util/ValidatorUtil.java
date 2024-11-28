package com.xlw.test.template_demo.util;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:
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
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 0);
        System.out.println(map);
        map.computeIfPresent("a", (k, v) -> 1);
        System.out.println(map);
    }
}
