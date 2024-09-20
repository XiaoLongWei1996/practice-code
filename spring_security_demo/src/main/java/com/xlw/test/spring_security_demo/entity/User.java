package com.xlw.test.spring_security_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * @description:
 * @Title: User
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.entity
 * @Date 2024/9/20 22:01
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String username;

    private String password;

    private String realName;

    private Integer age;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;
}
