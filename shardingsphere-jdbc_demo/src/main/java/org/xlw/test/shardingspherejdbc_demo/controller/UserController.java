package org.xlw.test.shardingspherejdbc_demo.controller;


import org.springframework.web.bind.annotation.*;
import org.xlw.test.shardingspherejdbc_demo.entity.User;
import org.xlw.test.shardingspherejdbc_demo.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表(User)表控制层
 *
 * @author xlw
 * @since 2024-03-14 16:45:46
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("selectAll")
    public List<User> selectAll() {
        return userService.list();
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public User selectOne(@PathVariable Integer id) {
        return userService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @PostMapping("save")
    public Boolean insert(User user) {
        return userService.save(user);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public Boolean delete(List<Integer> idList) {
        return userService.removeByIds(idList);
    }
	
}

