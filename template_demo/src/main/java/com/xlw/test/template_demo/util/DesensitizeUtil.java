package com.xlw.test.template_demo.util;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.xlw.test.template_demo.entity.Student;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 脱敏工具类
 * @Title: DesensitizeUtil
 * @Author xlw
 * @Package com.xlw.test.template_demo.util
 * @Date 2025/1/23 13:54
 */
public class DesensitizeUtil {

    /**
     * 对象脱敏
     *
     * @param t
     */
    public static <T> void objectDesensitize(T t) {
        Assert.notNull(t, "t must not be null");
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            DesensitizeUtil.Desensitize annotation = field.getDeclaredAnnotation(DesensitizeUtil.Desensitize.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            DesensitizeType desensitizeType = annotation.value();
            if (DesensitizeType.NONE.equals(desensitizeType)) {
                continue;
            }
            if (String.class.isAssignableFrom(field.getType())) {
                try {
                    String oldValue = (String) field.get(t);
                    String newValue = desensitize(oldValue, annotation.value(), annotation.startInclude(), annotation.endExclude());
                    field.set(t, newValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 集合脱敏
     *
     * @param collection 收集
     */
    public static <T> void collectionDesensitize(Collection<T> collection) {
        Assert.notEmpty(collection, "collection must not be null");
        try {
            T t = collection.stream().findFirst().get();
            Class<T> clazz = (Class<T>) t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                DesensitizeUtil.Desensitize annotation = field.getDeclaredAnnotation(DesensitizeUtil.Desensitize.class);
                if (Objects.isNull(annotation) || DesensitizeType.NONE.equals(annotation.value())) {
                    continue;
                }
                if (String.class.isAssignableFrom(field.getType())) {
                    for (T e : collection) {
                        String oldValue = (String) field.get(e);
                        String newValue = desensitize(oldValue, annotation.value(), annotation.startInclude(), annotation.endExclude());
                        field.set(e, newValue);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * map脱敏
     *
     * @param map 地图
     */
    public static <T> void mapDesensitize(Map<?, T> map) {
        Collection<T> values = map.values();
        collectionDesensitize(values);
    }

    /**
     * 脱 敏
     *
     * @param str             str
     * @param desensitizeType Desensitize 类型
     * @param startInclude    开始包含
     * @param endExclude      结束排除
     * @return {@link String }
     */
    private static String desensitize(String str, DesensitizeType desensitizeType, int startInclude, int endExclude) {
        switch (desensitizeType) {
            case PHONE:
                return DesensitizedUtil.mobilePhone(str);
            case ID_CARD:
                return DesensitizedUtil.idCardNum(str, 3, 4);
            case BANK_CARD:
                return DesensitizedUtil.bankCard(str);
            case ADDRESS:
                return DesensitizedUtil.address(str, 8);
            case EMAIL:
                return DesensitizedUtil.email(str);
            case NAME:
                return DesensitizedUtil.desensitized(str, DesensitizedUtil.DesensitizedType.CHINESE_NAME);
            case PASSWORD:
                return DesensitizedUtil.password(str);
            case CUSTOMIZE:
                return StrUtil.hide(str, startInclude, str.length() >= endExclude ? str.length() - endExclude : str.length());
            default:
                return str;
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Desensitize {
        /**
         * 脱敏类型
         *
         * @return {@link DesensitizeType }
         */
        DesensitizeType value() default DesensitizeType.NONE;

        /**
         * 前置包含的位数，CUSTOMIZE使用
         *
         * @return int
         */
        int startInclude() default 0;

        /**
         * 后置包含的位数，CUSTOMIZE使用
         *
         * @return int
         */
        int endExclude() default 0;
    }

    public enum DesensitizeType {
        /**
         * 无
         */
        NONE,
        /**
         * 电话
         */
        PHONE,
        /**
         * 身份证
         */
        ID_CARD,
        /**
         * 银行卡
         */
        BANK_CARD,
        /**
         * 地址
         */
        ADDRESS,
        /**
         * 电子邮件
         */
        EMAIL,
        /**
         * 名字
         */
        NAME,
        /**
         * 密码
         */
        PASSWORD,
        /**
         * 自定义
         */
        CUSTOMIZE,
    }

    public static void main(String[] args) {
        Map<String, Student> map = new HashMap<>();
        Student student = new Student();
        student.setName("张伟");
        student.setBirthday(LocalDateTime.now());
        student.setIdcard("42028119960923501X");
        map.put("1", student);
        mapDesensitize(map);
        System.out.println(map);
    }
}
