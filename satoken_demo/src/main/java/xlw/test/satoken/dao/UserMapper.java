package xlw.test.satoken.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Select;
import xlw.test.satoken.entity.User;

/**
 * 用户表(User)表数据库访问层
 *
 * @author xlw
 * @since 2024-03-23 15:25:18
 */
public interface UserMapper extends BaseMapper<User> {
    
    @Select("select name age phone user_name password status lock_accont last_dt ip disable disable_dt enable_dt create_dt update_dt  from user")
    IPage<User> listByPage(IPage<?> page);
    
}

