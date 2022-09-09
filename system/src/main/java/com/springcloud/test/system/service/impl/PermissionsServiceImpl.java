package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.PermissionsMapper;
import com.springcloud.test.system.entity.Permissions;
import com.springcloud.test.system.service.PermissionsService;
import org.springframework.stereotype.Service;

/**
 * 权限(Permissions)表服务实现类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Service("permissionsService")
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements PermissionsService {

}

