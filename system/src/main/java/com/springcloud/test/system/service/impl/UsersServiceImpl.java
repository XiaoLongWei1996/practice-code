package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.UsersMapper;
import com.springcloud.test.system.entity.Users;
import com.springcloud.test.system.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * 用户表(Users)表服务实现类
 *
 * @author xlw
 * @since 2022-09-08 15:02:46
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}

