package com.xlw.kafka_demo.entity;

import lombok.Data;

/**
 * @Title: Result
 * @Author xlw
 * @Package com.xlw.kafka_demo.entity
 * @Date 2023/6/25 10:10
 * @description: 返回结果
 */
@Data
public class Result<T> {

    private Integer code;

    private T body;

    private String message;

    public Result(Integer code, T body, String message) {
        this.code = code;
        this.body = body;
        this.message = message;
    }

    public static <T> Result succeed(T t) {
        return new Result(200,t, "成功");
    }

    public static <T> Result fail(T t) {
        return new Result(400, t, "失败");
    }
}
