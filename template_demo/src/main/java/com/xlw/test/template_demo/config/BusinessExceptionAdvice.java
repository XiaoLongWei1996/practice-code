package com.xlw.test.template_demo.config;

import com.xlw.test.template_demo.cons.Result;
import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.util.ServletEnhanceUtil;
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
    public Result<?> response(BusinessException e) {
        errorInfo(e);
        return Result.fail(e.getMessage());
    }

    /**
     * 对象参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handle(BindException e) {
        errorInfo(e);
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
    public Result<?> handle(ConstraintViolationException e) {
        errorInfo(e);
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
    public Result<?> handle(MethodArgumentNotValidException e) {
        errorInfo(e);
        String info = e.getBindingResult().getFieldErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(";"));
        return Result.fail(info);
    }

    /**
     * 错误信息,记录
     *
     * @param e e
     */
    private void requestInfo(Exception e) {
        log.error("客户端Ip:{},请求方式:{},请求路径:{},参数:{},请求体:{}", ServletEnhanceUtil.getClientIp(),
                ServletEnhanceUtil.getMethod(), ServletEnhanceUtil.getUri(), ServletEnhanceUtil.getParameter(),ServletEnhanceUtil.getBody());
    }

    /**
     * 错误信息
     *
     * @param e e
     */
    private void errorInfo(Exception e) {
        requestInfo(e);
        log.error("请求异常:", e);
    }
}
