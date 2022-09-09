package com.springcloud.test.system.config.shiro;

import cn.hutool.core.util.ObjectUtil;
import com.springcloud.test.system.entity.Users;
import org.apache.shiro.SecurityUtils;

/**
 * @author 肖龙威
 * @date 2022/09/09 15:55
 */
public class TokenManager {

    public static Users getCurrentUser() {
        Object o = SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtil.isEmpty(o)) {
            throw new RuntimeException("当前用户已经失效");
        }
        return (Users) o;
    }

}
