package com.xlw.kafka_demo.exception;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * @Title: ResultException
 * @Author xlw
 * @Package com.xlw.kafka_demo.exception
 * @Date 2023/6/25 10:44
 * @description: 自定义处理结果异常
 */
@Getter
public class ResultException extends RuntimeException{

    private Supplier supplier;

    public ResultException(String message, Supplier supplier) {
        super(message);
        this.supplier = supplier;
    }

    public static ResultException create(Supplier supplier) {
        return new ResultException("查询数据不存在", supplier);
    }

    public static <T> ResultException create(T t) {
        return new ResultException("查询数据不存在", () -> t);
    }
}
