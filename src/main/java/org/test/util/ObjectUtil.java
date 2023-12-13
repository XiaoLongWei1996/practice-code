package org.test.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @description: 对象处理工具
 * @Title: ObjectUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2023/12/13 14:33
 */
public class ObjectUtil {

    public static <T> T convertObject(Object o, Class<T> clazz) {
        if (o == null || clazz == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Class<?> aClass = o.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fName = field.getName();
            try {
                Method setMethod = clazz.getMethod("set" + fName.substring(0, 1).toUpperCase() + fName.substring(1), field.getType());
                Method getMethod = aClass.getMethod("get" + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                setMethod.setAccessible(true);
                getMethod.setAccessible(true);
                setMethod.invoke(t, getMethod.invoke(o));
            } catch (NoSuchMethodException e) {

            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }

    public static void main(String[] args) {

    }
}

class Ss {
    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Ss{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class Se {
    private String name;

    private Integer age;

    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Se{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", date=" + date +
                '}';
    }
}
