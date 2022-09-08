package com.springcloud.test.system.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springcloud.test.system.dao.UsersMapper;
import com.springcloud.test.system.entity.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户表(Users)表控制层
 *
 * @author xlw
 * @since 2022-09-08 15:02:46
 */
@Api(tags = "用户")
@RestController
@RequestMapping("users")
public class UsersController {
    /**
     * 服务对象
     */
    @Resource
    private UsersMapper usersMapper;

    @ApiOperation(value = "登录", notes = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("login")
    public ResponseEntity<String> login(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new RuntimeException("用户名或者密码不能为空");
        }
        //当前用户对象
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(userName, password));
        Users users = (Users) subject.getPrincipal();
        if (ObjectUtil.isNotEmpty(users)) {
            users.setState(1);
            users.setLoginDt(new Date());
            usersMapper.updateById(users);
        }
        return ResponseEntity.ok("登录成功");
    }

    @ApiOperation(value = "登出", notes = "登出")
    @GetMapping("logout")
    public ResponseEntity<String> logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Users users = (Users) subject.getPrincipal();
        if (ObjectUtil.isNotEmpty(users)) {
            users.setState(0);
            users.setLogoutDt(new Date());
            usersMapper.updateById(users);
        }
        return ResponseEntity.ok("等出成功");
    }

    /**
     * 新增数据
     *
     * @param users 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "注册用户", notes = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "birth", value = "生日", required = true, dataType = "Date", dataTypeClass = Date.class)
    })
    @PostMapping("register")
    public ResponseEntity<Integer> register(Users users) {
        Users u = usersMapper.selectOne(new QueryWrapper<Users>().eq("user_name", users.getUserName()));
        if (ObjectUtil.isNotEmpty(u)) {
            throw new RuntimeException("该用户名已经存在");
        }
        if (StrUtil.isBlank(users.getUserName()) || StrUtil.isBlank(users.getPassword())) {
            throw new RuntimeException("用户名或者密码不能为空");
        }
        //生成盐
        String salt = RandomUtil.randomString(8);
        //密码加密
        Md5Hash md5Hash = new Md5Hash(users.getPassword(), salt, 1024);
        users.setPassword(md5Hash.toHex());
        users.setSalt(salt);
        usersMapper.insert(users);
        return ResponseEntity.ok(1);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "删除用户", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
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
    @ApiOperation(value = "修改用户", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "birth", value = "生日", required = true, dataType = "Date", dataTypeClass = Date.class)
    })
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
    @ApiOperation(value = "查询所有用户", notes = "查询")
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
    @ApiOperation(value = "查询单个用户", notes = "查询")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @GetMapping("selectOne")
    public ResponseEntity<Users> selectOne(Integer id) {
        Users users = usersMapper.selectById(id);
        return ResponseEntity.ok(users);
    }

}

