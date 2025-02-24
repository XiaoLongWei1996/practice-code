package com.xlw.test.template_demo.util;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.Opcodes;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * @description: 反射工具类
 * @Title: ReflectUtil
 * @Author xlw
 * @Package com.xlw.test.template_demo.util
 * @Date 2025/1/16 10:15
 */
public class ReflectUtil {

    private static final Map<String, Function> WEAK_MAP = new WeakHashMap<>();

    /**
     * 将bean的属性的get/set方法，作为lambda表达式传入时，获取get/set方法对应的属性Field
     *
     * @param fn  lambda表达式，bean的属性的get方法
     * @param <T> 泛型
     * @return 属性对象
     */
    public static <T> Field getField(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        //从lambda信息取出method、field、class等
        String implMethodName = serializedLambda.getImplMethodName();
        // 确保方法是符合规范的get/set方法，boolean类型是is开头
        if (!implMethodName.startsWith("is") && !implMethodName.startsWith("get") && !implMethodName.startsWith("set")) {
            throw new RuntimeException("get方法名称: " + implMethodName + ", 不符合java bean规范");
        }

        // get方法开头为 is 或者 get，将方法名 去除is或者get，然后首字母小写，就是属性名
        int prefixLen = implMethodName.startsWith("is") ? 2 : 3;

        String fieldName = implMethodName.substring(prefixLen);
        String firstChar = fieldName.substring(0, 1);
        fieldName = fieldName.replaceFirst(firstChar, firstChar.toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

    /**
     * ASM获取字段名称
     *
     * @param classBytes 类字节
     * @return {@link List }<{@link String }>
     */
    public static List<String> getFieldName(byte[] classBytes) {
        List<String> fields = new ArrayList<>();
        ClassReader reader = new ClassReader(classBytes);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(
                    int access,      // 访问修饰符（如public/private）
                    String name,    // 字段名
                    String descriptor, // 字段类型描述符（如"I"表示int）
                    String signature,  // 泛型信息（可为null）
                    Object value      // 初始值（常量字段）
            ) {
                // 将字段信息转换为可读字符串
                String fieldInfo = String.format(
                        "字段名: %s, 类型: %s, 修饰符: %s",
                        name, descriptor, access
                );
                fields.add(fieldInfo);
                return super.visitField(access, name, descriptor, signature, value);
            }
        };
        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        return fields;
    }

    /**
     * 获取字段值
     *
     * @param t          t
     * @param fieldName  字段名称
     * @param returnType 返回类型
     * @return {@link R }
     */
    public static <T, R> R getFieldValue(T t, String fieldName, Class<R> returnType) {
        String key = t.getClass().getName() + "." + fieldName;
        Function<T, R> function = WEAK_MAP.get(key);
        if (function == null) {
            try {
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle methodHandle = lookup.findVirtual(t.getClass(), methodName, MethodType.methodType(returnType));
                // 创建 Lambda 表达式
                CallSite callSite = LambdaMetafactory.metafactory(
                        lookup,
                        "apply", // 目标方法名
                        MethodType.methodType(Function.class), // Lambda 类型
                        MethodType.methodType(Object.class, Object.class), // 函数签名
                        methodHandle, // 目标方法句柄
                        methodHandle.type() // 目标方法类型
                );
                // 获取 Lambda 实例
                function = (Function<T, R>) callSite.getTarget().invoke();
                WEAK_MAP.put(key, function);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return function.apply(t);
    }

    /**
     * 获取字段值
     *
     * @param t  t
     * @param fn fn
     * @return {@link R }
     */
    public static <T, R> R getFieldValue(T t, Function<T, R> fn) {
       return fn.apply(t);
    }

    public static <T> Object getFieldValue(T t, String fieldName) {
        Class<?> clazz = t.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(t);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
       List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        for (String s : list) {
            list.add(s + "a");
            System.out.println(s);
        }
    }
}
