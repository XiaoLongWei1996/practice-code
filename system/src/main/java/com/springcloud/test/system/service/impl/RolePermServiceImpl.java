package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.RolePermMapper;
import com.springcloud.test.system.entity.RolePerm;
import com.springcloud.test.system.service.RolePermService;
import org.springframework.stereotype.Service;

/**
 * 角色权限绑定(RolePerm)表服务实现类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Service("rolePermService")
public class RolePermServiceImpl extends ServiceImpl<RolePermMapper, RolePerm> implements RolePermService {

}

