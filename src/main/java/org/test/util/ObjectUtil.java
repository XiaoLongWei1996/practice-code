package org.test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        Parse p1 = new Parse(o.getClass());
        Parse p2 = new Parse(clazz);
        for (String fieldName : p2.getFieldNames()) {
            Method setMethod = p2.getSetMethod(fieldName);
            Method getMethod = p1.getGetMethod(fieldName);
            if (getMethod == null){
                continue;
            }
            try {
                setMethod.invoke(t, getMethod.invoke(o));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }

    public static <E,T> List<T> convertList(List<E> list, Class<E> eClass, Class<T> clazz){
        if (list == null || clazz == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        List<T> result = new ArrayList<>();
        Parse p1 = new Parse(eClass);
        Parse p2 = new Parse(clazz);
        T t = null;
        try {
            t = clazz.newInstance();
            for (E e : list) {
                Set<String> fieldNames = p2.getFieldNames();
                for (String fieldName : fieldNames) {
                    Method getMethod = p1.getGetMethod(fieldName);
                    if (getMethod == null) {
                        continue;
                    }
                    Method setMethod = p2.getSetMethod(fieldName);
                    setMethod.invoke(t, getMethod.invoke(e));
                }
                result.add(t);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static class Parse {

        private Map<String, Method> getMap;

        private Map<String, Method> setMap;

        public Parse(Class<?> clazz) {
            getMap = new HashMap<>();
            setMap = new HashMap<>();
            for (Method method : clazz.getDeclaredMethods()) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    setMap.put(methodName.substring(3), method);
                }

                if (methodName.startsWith("get")) {
                    getMap.put(methodName.substring(3), method);
                }
            }
        }

        public Method getSetMethod(String fName) {
            return setMap.get(fName);
        }

        public Method getGetMethod(String fName) {
            return getMap.get(fName);
        }

        public Set<String> getFieldNames() {
            Set<String> names = setMap.keySet();
            if (names == null) {
                throw new NullPointerException();
            }
            return names;
        }

    }

    public static void main(String[] args) {
        Ss ss = new Ss("小明", 12);
        Ss s1 = new Ss("小喇叭", 14);
        List<Ss> list = new ArrayList<>();
        list.add(ss);
        list.add(s1);
        List<Se> ses = ObjectUtil.convertList(list, Ss.class, Se.class);
        System.out.println(ses);


    }
}

class Ss {
    private String name;

    private Integer age;

    public Ss() {
    }

    public Ss(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

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

    public Se() {
    }

    public Se(String name, Integer age, Date date) {
        this.name = name;
        this.age = age;
        this.date = date;
    }

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
