package com.springcloud.test.system.controller;

import com.springcloud.test.system.entity.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理错误信息控制器
 * @author 肖龙威
 * @date 2022/09/08 16:37
 */
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> message(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(Result.fail(e.getMessage()));
    }
}
