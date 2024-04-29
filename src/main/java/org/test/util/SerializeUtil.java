package org.test.util;

import io.fury.Fury;
import io.fury.ThreadSafeFury;
import io.fury.config.Language;
import org.test.design.stream.Student;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @description: 序列化工具类
 * @Title: SerializeUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2024/4/29 16:15
 */
public class SerializeUtil {


    private static final Fury FURY = Fury.builder().withLanguage(Language.JAVA).build();

    private static ThreadSafeFury threadSafeFury;

    static {
        threadSafeFury = Fury.builder()
                .withLanguage(Language.JAVA)
                .withAsyncCompilation(true)
                .requireClassRegistration(true)
                .buildThreadSafeFuryPool(2, 4, 5, TimeUnit.SECONDS);

    }

    /**
     * 序列化
     *
     * @param o o
     * @return {@link byte[]}
     */
    public static byte[] serialize(Object o) {
        FURY.register(o.getClass());
        return FURY.serialize(o);
    }

    /**
     * 反序列化
     *
     * @param data 数据
     * @return {@link Object}
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        FURY.register(clazz);
        return (T) FURY.deserialize(data);
    }

    public static byte[] asyncSerialize(Object o) {
        FURY.register(o.getClass());
        return threadSafeFury.execute(fury -> {
            fury.register(o.getClass());
            return fury.serialize(o);
        });
    }

    public static <T> T asyncDeserialize(byte[] data, Class<T> clazz) {
        return (T) threadSafeFury.execute(fury -> {
            fury.register(clazz);
            return fury.deserialize(data);
        });
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("小明");
        byte[] serialize = SerializeUtil.asyncSerialize(student);
        System.out.println(Arrays.toString(serialize));
        Object o = SerializeUtil.asyncDeserialize(serialize, student.getClass());
        System.out.println(o);
    }


}
