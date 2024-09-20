package com.xlw.test.spring_security_demo.config;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 肖龙威
 * @date 2022/12/01 13:12
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String pwd = DigestUtil.md5Hex(rawPassword.toString());
        return pwd;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(encodedPassword, encode(rawPassword));
    }
}
