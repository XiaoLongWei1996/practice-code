package com.xlw.test.log4j2_test.entity;

import lombok.Getter;

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

    private Integer code;

    private T data;

    private String message;

    public Result(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, data, "请求成功");
    }

    public static <T> Result<T> success(Supplier<T> supplier) {
        return new Result<>(200, supplier.get(), "请求成功");
    }

    public static <T> Result<T> fail(T data)  {
        return new Result<>(400, data, "请求失败");
    }

    public static <T> Result<T> fail(Supplier<T> supplier) {
        return new Result<>(400, supplier.get(), "请求失败");
    }

    public static <T> Result<T> result(Integer code, T data, String message) {
        return new Result<>(code, data, message);
    }

}
