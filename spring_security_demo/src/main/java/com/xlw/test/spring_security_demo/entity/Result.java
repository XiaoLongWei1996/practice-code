package com.xlw.test.spring_security_demo.entity;

import lombok.Data;

import java.util.function.Supplier;

/**
 * 结果
 *
 * @description: 返回结果
 * @Title: Result
 * @Author xlw
 * @Package org.xlw.test.entity
 * @Date 2023/8/18 15:59
 */
@Data
public class Result<T> {

    /**
     * 请求成功
     */
    public static final Integer SUCCESS = 0;

    /**
     * 请求失败
     */
    public static final Integer FAIL = -1;

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
        return new Result<>(SUCCESS, data, "请求成功");
    }

    /**
     * 成功
     *
     * @param data    数据
     * @param message 消息
     * @return {@link Result}
     */
    public static <T> Result<T>  success(T data, String message) {
        return new Result<>(SUCCESS, data, message);
    }

    /**
     * 成功
     *
     * @param supplier 供应商
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> success(Supplier<T> supplier) {
        return new Result<>(SUCCESS, supplier.get(), "请求成功");
    }

    /**
     * 失败
     *
     * @param data 数据
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> fail(T data) {
        return new Result<>(FAIL, data, "请求失败");
    }

    /**
     * 失败
     *
     * @param message 消息
     * @return {@link Result }
     */
    public static Result fail(String message) {
        return new Result<>(FAIL, null, message);
    }

    /**
     * 失败
     *
     * @return {@link Result}
     */
    public static Result fail() {
        return new Result(FAIL, null, "请求失败");
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

}
