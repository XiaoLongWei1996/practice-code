package com.xlw.test.jsr303_demo.config;

import cn.hutool.http.HttpStatus;
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
        return new Result<>(HttpStatus.HTTP_OK, data, "请求成功");
    }

    public static <T> Result<T> success(Supplier<T> supplier) {
        return new Result<>(HttpStatus.HTTP_OK, supplier.get(), "请求成功");
    }

    public static <T> Result<T> fail(T data)  {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, data, "请求失败");
    }

    public static <T> Result<T> fail(Supplier<T> supplier) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, supplier.get(), "请求失败");
    }

    public static <T> Result<T> result(Integer code, T data, String message) {
        return new Result<>(code, data, message);
    }

}
