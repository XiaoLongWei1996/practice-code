package com.xlw.test.log4j2_test.entity;

import cn.hutool.http.HttpStatus;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;

/**
 * @description: 返回结果
 * @Title: Result
 * @Author xlw
 * @Package org.xlw.test.entity
 * @Date 2023/8/18 15:59
 */
@Getter
public class Result<T> {

    /**
     * 代码
     */
    private Integer code;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    /**
     * 结果
     *
     * @param code    代码
     * @param data    数据
     * @param message 消息
     */
    public Result(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.HTTP_OK, data, "请求成功");
    }

    /**
     * 成功
     *
     * @return {@link Result}
     */
    public static Result success() {
        return new Result<>(HttpStatus.HTTP_OK, true, "请求成功");
    }

    /**
     * 成功
     *
     * @param data  数据
     * @param clazz clazz
     * @return {@link Result}<{@link T}>
     */

    public static <T> Result<T> success(T data, Class<T> clazz) {
        return new Result<>(HttpStatus.HTTP_OK, getDefault(data, clazz), "请求成功");
    }

    /**
     * 成功
     *
     * @param supplier 供应商
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> success(Supplier<T> supplier) {
        return new Result<>(HttpStatus.HTTP_OK, supplier.get(), "请求成功");
    }

    /**
     * 失败
     *
     * @param data 数据
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> fail(T data) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, data, "请求失败");
    }

    public static <T> Result<T> fail(T data, Class<T> clazz) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, getDefault(data, clazz), "请求失败");
    }

    /**
     * 失败
     *
     * @return {@link Result}
     */
    public static Result fail() {
        return new Result(HttpStatus.HTTP_BAD_REQUEST, false, "请求失败");
    }

    /**
     * 失败
     *
     * @param supplier 供应商
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> fail(Supplier<T> supplier) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, supplier.get(), "请求失败");
    }

    /**
     * 结果
     *
     * @param code    代码
     * @param data    数据
     * @param message 消息
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> result(Integer code, T data, String message) {
        return new Result<>(code, data, message);
    }

    private static <T> T getDefault(T t, Class<T> clazz) {
        return Optional.ofNullable(t).orElseGet(() -> {
            if (List.class.isAssignableFrom(clazz)) {
                return (T) new ArrayList<>();
            } else if (Set.class.isAssignableFrom(clazz)) {
                return (T) new HashSet<>();
            } else if (Map.class.isAssignableFrom(clazz)) {
                return (T) new HashMap<>();
            } else {
                try {
                    return clazz.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
