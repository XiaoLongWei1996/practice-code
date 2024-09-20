package com.xlw.test.spring_security_demo.entity;

import lombok.Data;

/**
 * @description:
 * @Title: User
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.entity
 * @Date 2024/9/20 17:52
 */
@Data
public class LoginUser {

    private String userName;

    private String password;
}
