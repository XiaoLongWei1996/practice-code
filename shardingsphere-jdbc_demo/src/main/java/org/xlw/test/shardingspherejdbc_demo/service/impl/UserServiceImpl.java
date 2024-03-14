package org.xlw.test.shardingspherejdbc_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xlw.test.shardingspherejdbc_demo.dao.UserMapper;
import org.xlw.test.shardingspherejdbc_demo.entity.User;
import org.xlw.test.shardingspherejdbc_demo.service.UserService;

/**
 * 用户表(User)表服务实现类
 *
 * @author xlw
 * @since 2024-03-14 16:45:52
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

