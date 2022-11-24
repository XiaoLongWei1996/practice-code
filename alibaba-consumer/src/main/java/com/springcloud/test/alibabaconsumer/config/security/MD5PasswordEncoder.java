package com.springcloud.test.alibabaconsumer.config.security;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 肖龙威
 * @date 2022/11/24 15:54
 */
@Component
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
