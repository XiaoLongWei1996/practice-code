package com.springcloud.test.alibabaconsumer.controller;


import com.springcloud.test.alibabaconsumer.dao.UsersMapper;
import com.springcloud.test.alibabaconsumer.entity.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户表(Users)表控制层
 *
 * @author xlw
 * @since 2022-11-24 15:22:20
 */
@RestController
@RequestMapping("users")
public class UsersController {
    /**
     * 服务对象
     */
    @Resource
    private UsersMapper usersMapper;

    /**
     * 新增数据
     *
     * @param users 实体对象
     * @return 新增结果
     */
    @PostMapping("save")
    public ResponseEntity<Integer> save(Users users) {
        usersMapper.insert(users);
        return ResponseEntity.ok(1);
    }
    
     /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(Integer id) {
        usersMapper.deleteById(id);
        return ResponseEntity.ok(1);
    }
    
    /**
     * 修改数据
     *
     * @param users 实体对象
     * @return 修改结果
     */
    @PutMapping("update")
    public ResponseEntity<Integer> update(Users users) {
        usersMapper.updateById(users);
        return ResponseEntity.ok(1);
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("selectAll")
    public ResponseEntity<List<Users>> selectAll() {
        List<Users> list = usersMapper.selectList(null);
        return ResponseEntity.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ResponseEntity<Users> selectOne(Integer id) {
        Users users = usersMapper.selectById(id);
        return ResponseEntity.ok(users);
    }

}

