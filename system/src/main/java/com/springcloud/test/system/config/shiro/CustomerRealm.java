package com.springcloud.test.system.config.shiro;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springcloud.test.system.dao.PermissionsMapper;
import com.springcloud.test.system.dao.RoleMapper;
import com.springcloud.test.system.entity.Role;
import com.springcloud.test.system.entity.Users;
import com.springcloud.test.system.service.UsersService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 肖龙威
 * @date 2022/09/08 14:47
 */
public class CustomerRealm extends AuthorizingRealm {

    @Autowired
    private UsersService usersService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionsMapper permissionsMapper;

    /**
     * 处理授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String principal = (String) principalCollection.getPrimaryPrincipal();
        Users u = usersService.getOne(new QueryWrapper<Users>().eq("user_name", principal));
        if (ObjectUtil.isEmpty(u)) {
            return null;
        }
        List<Role> roles = roleMapper.selectAllByUserId(u.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<String>();

        return info;
    }

    /**
     * 处理认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //用户名
        String principal = (String) authenticationToken.getPrincipal();
        Users user = usersService.getOne(new QueryWrapper<Users>().eq("user_name", principal));
        if (ObjectUtil.isEmpty(user)) {
            return null;
        }
        AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return info;
    }
}
