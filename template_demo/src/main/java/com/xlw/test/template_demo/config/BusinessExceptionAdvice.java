package com.xlw.test.template_demo.config;

import com.xlw.test.template_demo.cons.Result;
import com.xlw.test.template_demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @description: 异常处理
 * @Title: BusinessExceptionAdvice
 * @Author xlw
 * @Package com.sxkj.pay.demos.web.config
 * @Date 2024/8/7 16:43
 */
@Slf4j
@RestControllerAdvice
public class BusinessExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result<String> response(BusinessException e) {
        log.error("业务异常:", e);
        return Result.fail(e.getMessage());
    }

    /**
     * 对象参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<String> handle(BindException e) {
        log.error("数据校验异常:", e);
        String info = e.getFieldErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(";"));
        return Result.fail(info);
    }

    /**
     * 路径参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handle(ConstraintViolationException e) {
        log.error("数据校验异常:", e);
        String info = e.getConstraintViolations().stream().map(o -> o.getMessage()).collect(Collectors.joining(";"));
        return Result.fail(info);
    }

    /**
     * json请求的参数检验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handle(MethodArgumentNotValidException e) {
        log.error("数据校验异常:", e);
        String info = e.getBindingResult().getFieldErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(";"));
        return Result.fail(info);
    }

}
