package com.xlw.test.template_demo.util;

/**
 * @description: 线程本地工具类
 * @Title: ThreadLocalUtil
 * @Author xlw
 * @Package com.sxkj.pay.util
 * @Date 2024/8/8 17:00
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<Object> THREADLOCAL = new ThreadLocal<>();

    public static void set(Object object) {
        THREADLOCAL.set(object);
    }

    public static Object get() {
        return THREADLOCAL.get();
    }

    public static void remove() {
        THREADLOCAL.remove();
    }
}
