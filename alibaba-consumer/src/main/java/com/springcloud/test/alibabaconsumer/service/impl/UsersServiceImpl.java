package com.springcloud.test.alibabaconsumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.alibabaconsumer.dao.UsersMapper;
import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * 用户表(Users)表服务实现类
 *
 * @author xlw
 * @since 2022-11-24 15:22:23
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}

