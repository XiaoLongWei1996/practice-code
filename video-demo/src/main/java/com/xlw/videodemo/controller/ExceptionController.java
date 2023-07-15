package com.xlw.videodemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 异常处理
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.videodemo.controller
 * @Date 2023/7/15 22:11
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public String ex(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return "访问失败";
    }
}
