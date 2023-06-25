package com.xlw.kafka_demo.controller;

import com.xlw.kafka_demo.entity.Result;
import com.xlw.kafka_demo.exception.ResultException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.kafka_demo.controller
 * @Date 2023/6/25 9:58
 * @description: 异常处理器
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BindException.class)
    public Result<Map> handle(BindException e, BindingResult bindingResult) {
        e.printStackTrace();
        Map<String, String> data = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            data.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Result.fail(data);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Map> handle(ConstraintViolationException e) {
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Map<String, String> data = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String path = constraintViolation.getPropertyPath().toString();
            String[] ss = path.split("\\.");
            data.put(ss[ss.length - 1], constraintViolation.getMessage());
        }
        return Result.fail(data);
    }

    @ExceptionHandler(ResultException.class)
    public Result<Object> handle1(ResultException e) {
        e.printStackTrace();
        return Result.fail(e.getSupplier().get());
    }
}
