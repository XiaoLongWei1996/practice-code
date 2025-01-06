package com.xlw.test.template_demo.util;


import cn.hutool.core.lang.Assert;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @description: 枚举工具类
 * @Title: EnumUtils
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2025/1/6 17:22
 */
public class EnumUtils {

    /**
     * 获取枚举实例
     *
     * @param enumClass enum 类
     * @param fieldName 字段名称
     * @param value     值
     * @return {@link T }
     */
    public static <T extends Enum<T>> T getEnumInstance(Class<T> enumClass, String fieldName, Object value) {
        Assert.notNull(enumClass, "enumClass must not be null");
        Assert.notNull(fieldName, "fieldName must not be null");
        Assert.notNull(value, "value must not be null");
        try {
            Field field = enumClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            T[] enumConstants = enumClass.getEnumConstants();
            Assert.notNull(enumConstants, "enumConstants is null");
            for (T enumConstant : enumConstants) {
                Object fieldValue = field.get(enumConstant);
                if (Objects.equals(fieldValue, value)) {
                    return enumConstant;
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
