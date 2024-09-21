package com.xlw.test.spring_security_demo.controller;

import com.xlw.test.spring_security_demo.config.AuthException;
import com.xlw.test.spring_security_demo.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description:
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.controller
 * @Date 2024/9/21 9:57
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AuthException.class)
    public Result<Void> handle(AuthException e) {
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }
}
