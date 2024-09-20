package com.xlw.test.redis_cache_test.controller;

import com.xlw.test.redis_cache_test.config.exception.RateLimiterException;
import com.xlw.test.redis_cache_test.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 异常处理
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.controller
 * @Date 2024/1/31 15:35
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RateLimiterException.class)
    public Result<String> exceptionHandler(RateLimiterException e) {
        return Result.fail(e.getMessage());
    }
}
