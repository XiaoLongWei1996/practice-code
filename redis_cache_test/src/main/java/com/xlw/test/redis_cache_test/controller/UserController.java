package com.xlw.test.redis_cache_test.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlw.test.redis_cache_test.entity.Result;
import com.xlw.test.redis_cache_test.entity.User;
import com.xlw.test.redis_cache_test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表(User)表控制层
 *
 * @author xlw
 * @since 2023-12-23 19:43:50
 */
@Slf4j
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
    public Result<List<User>> selectAll() {
        return Result.succeed(userService.list());
    }
    
    /**
     * 分页查询所有数据
     *
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@link Result}<{@link IPage}<{@link User}>>
     */
    @GetMapping("selectAllByPage")
    public Result<IPage<User>> selectAllByPage(Integer currentPage, Integer pageSize) {
        IPage page = Page.of(currentPage, pageSize);
        return Result.succeed(userService.listByPage(page));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public Result<User> selectOne(@PathVariable Integer id) {
        return Result.succeed(userService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @PostMapping("save")
    public Result<Boolean> insert(User user) {
        return Result.succeed(userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @PutMapping("update")
    public Result<Boolean> update(User user) {
        return Result.succeed(userService.update(user));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public Result<Boolean> delete(Integer id) {
        return Result.succeed(userService.delete(id));
    }
	
}

