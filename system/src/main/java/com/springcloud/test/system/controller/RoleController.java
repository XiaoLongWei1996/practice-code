package com.springcloud.test.system.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springcloud.test.system.dao.RoleMapper;
import com.springcloud.test.system.dao.UsersMapper;
import com.springcloud.test.system.entity.Role;
import com.springcloud.test.system.entity.RolePerm;
import com.springcloud.test.system.entity.UserRole;
import com.springcloud.test.system.entity.Users;
import com.springcloud.test.system.service.RolePermService;
import com.springcloud.test.system.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 角色(Role)表控制层
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Api(tags = "角色")
@RestController
@RequestMapping("role")
public class RoleController {
    /**
     * 服务对象
     */
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermService rolePermService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UsersMapper usersMapper;

    /**
     * 新增数据
     *
     * @param role 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增角色", notes = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("save")
    public ResponseEntity<Integer> save(Role role) {
        roleMapper.insert(role);
        return ResponseEntity.ok(1);
    }

    @ApiOperation(value = "角色授权", notes = "角色授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "permIds", value = "权限id数组", required = true, dataType = "int", dataTypeClass = int.class)
    })
    @RequiresRoles({"admin"})
    @PostMapping("grant")
    public ResponseEntity<Integer> grant(Integer roleId, Integer[] permIds) {
        Role role = roleMapper.selectById(roleId);
        Assert.notNull(role, "角色不存在");
        Assert.notNull(permIds, "未选择权限");
        rolePermService.remove(new QueryWrapper<RolePerm>().eq("role_id", roleId));
        List<RolePerm> list = new ArrayList<RolePerm>();
        for (Integer permId : permIds) {
            RolePerm rp = new RolePerm();
            rp.setRoleId(roleId);
            rp.setPermissionsId(permId);
            list.add(rp);
        }
        rolePermService.saveBatch(list);
        return ResponseEntity.ok(1);
    }

    @ApiOperation(value = "分配用户", notes = "分配用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "roleIds", value = "角色id数组", required = true, dataType = "int", dataTypeClass = int.class)
    })
    @RequiresRoles({"admin"})
    @PostMapping("allocation")
    public ResponseEntity<Integer> allocation(Integer userId, Integer[] roleIds) {
        Users users = usersMapper.selectById(userId);
        Assert.notNull(users, "用户不存在");
        Assert.notNull(roleIds, "未选择角色");
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        List<UserRole> list = new ArrayList<UserRole>();
        for (Integer roleId : roleIds) {
            UserRole ur = new UserRole();
            ur.setRoleId(roleId);
            ur.setUserId(userId);
            list.add(ur);
        }
        userRoleService.saveBatch(list);
        return ResponseEntity.ok(1);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "删除角色", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(Integer id) {
        roleMapper.deleteById(id);
        return ResponseEntity.ok(1);
    }

    /**
     * 修改数据
     *
     * @param role 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改角色", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PutMapping("update")
    public ResponseEntity<Integer> update(Role role) {
        roleMapper.updateById(role);
        return ResponseEntity.ok(1);
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @ApiOperation(value = "查询所有角色", notes = "查询")
    @GetMapping("selectAll")
    public ResponseEntity<List<Role>> selectAll() {
        List<Role> list = roleMapper.selectList(null);
        return ResponseEntity.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "查询单个角色", notes = "查询")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @GetMapping("selectOne")
    public ResponseEntity<Role> selectOne(Integer id) {
        Role role = roleMapper.selectById(id);
        return ResponseEntity.ok(role);
    }

}

