package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.RoleMapper;
import com.springcloud.test.system.entity.Role;
import com.springcloud.test.system.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色(Role)表服务实现类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}

