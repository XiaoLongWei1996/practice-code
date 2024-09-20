package com.xlw.test.spring_security_demo.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @description: 多功能认证
 * @Title: MultiAuthenticationToken
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config
 * @Date 2024/9/20 22:12
 */
public class MultiAuthenticationToken extends AbstractAuthenticationToken {

    private String principal;

    private String credentials;

    private String smsCode;

    /**
     * 登录类型,1:用户名密码登录,2:手机号登录
     */
    private int loginType;

    //认证前调用
    public MultiAuthenticationToken(String principal, String credentials, String smsCode, int loginType) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.smsCode = smsCode;
        this.loginType = loginType;
        setAuthenticated(false);
    }

    //确定认证时调用
    public MultiAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public int getLoginType() {
        return loginType;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
