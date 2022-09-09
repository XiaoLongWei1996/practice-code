package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.UserRoleMapper;
import com.springcloud.test.system.entity.UserRole;
import com.springcloud.test.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色绑定(UserRole)表服务实现类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

