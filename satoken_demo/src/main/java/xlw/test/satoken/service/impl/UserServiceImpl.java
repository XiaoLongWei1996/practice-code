package xlw.test.satoken.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xlw.test.satoken.dao.UserMapper;
import xlw.test.satoken.entity.User;
import xlw.test.satoken.service.UserService;

/**
 * 用户表(User)表服务实现类
 *
 * @author xlw
 * @since 2024-03-23 15:25:23
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public IPage<User> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }
}

