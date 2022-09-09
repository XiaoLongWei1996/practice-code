package com.springcloud.test.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springcloud.test.system.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色(Role)表数据库访问层
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select r.* from role r inner join user_role ur on r.id = ur.role_id and ur.user_id = #{userId}")
    List<Role> selectAllByUserId(@Param("userId") Integer userId);

}

