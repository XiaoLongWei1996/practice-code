package com.xlw.test.jsr303_demo.controller;

import com.xlw.test.jsr303_demo.User;
import com.xlw.test.jsr303_demo.config.Result;
import com.xlw.test.jsr303_demo.config.ValidGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @Title: UserController
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.controller
 * @Date 2024/2/3 15:56
 */
@Validated
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("t1")
    public Result<String> t1(@NotEmpty(message = "不能为空") String name) {
        return Result.success(name);
}

    @PostMapping("t2")
    public User t2(@Validated(ValidGroup.Insert.class) User user) {
        return user;
    }

    @PostMapping("t3")
    public User t3(@Validated(ValidGroup.Update.class) @RequestBody User user) {
        return user;
    }

    @GetMapping("t4/{id}")
    public Integer t4(@PathVariable("id") @Max(5) Integer id) {
        return id;
    }

}
