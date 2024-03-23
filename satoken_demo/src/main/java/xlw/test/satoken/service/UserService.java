package xlw.test.satoken.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xlw.test.satoken.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author xlw
 * @since 2024-03-23 15:25:22
 */
public interface UserService extends IService<User> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link User}>
     */
    IPage<User> listByPage(IPage<?> page);
}

