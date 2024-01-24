package com.xlw.test.log4j2_test.controller;

import com.xlw.test.log4j2_test.entity.Result;
import com.xlw.test.log4j2_test.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @Title: TestController
 * @Author xlw
 * @Package com.xlw.test.log4j2_test.controller
 * @Date 2023/9/21 16:43
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("t1")
    public Result<String> t1(String s) {
        return Result.success(s.toUpperCase());
    }

    @PostMapping("t2")
    public Result<Student> t2(Student student) {
        return Result.success(student);
    }

    @PostMapping("t3")
    public Result<String> t3() {
        return Result.success("ok");
    }

    @PostMapping("t4")
    public Result<String> t4() {
        int i = 1 / 0;
        return Result.success("ok");
    }
}
