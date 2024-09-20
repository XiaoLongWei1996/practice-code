package com.xlw.test.log4j2_test.config;

import com.xlw.test.log4j2_test.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 异常处理
 * @Title: ExceptionHandler
 * @Author xlw
 * @Package com.xlw.test.search.controller
 * @Date 2023/8/23 14:57
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public Result ex(Exception e) {
        return Result.fail("");
    }

}
