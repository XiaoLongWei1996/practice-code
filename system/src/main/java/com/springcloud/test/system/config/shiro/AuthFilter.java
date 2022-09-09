package com.springcloud.test.system.config.shiro;

import cn.hutool.core.lang.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义过滤器
 * @author 肖龙威
 * @date 2022/09/09 10:48
 */
public class AuthFilter extends AdviceFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        Assert.notNull(session, "用户登录已失效");
        return true;
    }
}
