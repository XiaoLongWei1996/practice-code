package com.xlw.test.jsr303_demo.controller;

import com.xlw.test.jsr303_demo.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description: 异常处理类
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.controller
 * @Date 2024/2/3 16:29
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    /**
     * 对象参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<Map> handle(BindException e) {
        log.error("字段错误", e);
        Map<String, String> data = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            data.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Result.fail(data);
    }

    /**
     * 路径参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Map> handle(ConstraintViolationException e) {
        log.error("字段错误", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Map<String, String> data = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String path = constraintViolation.getPropertyPath().toString();
            String[] ss = path.split("\\.");
            data.put(ss[ss.length - 1], constraintViolation.getMessage());
        }
        return Result.fail(data);
    }
}
