package com.springcloud.test.system.controller;


import com.springcloud.test.system.dao.PermissionsMapper;
import com.springcloud.test.system.entity.Permissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 权限(Permissions)表控制层
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@Api(tags = "权限")
@RestController
@RequestMapping("permissions")
public class PermissionsController {
    /**
     * 服务对象
     */
    @Resource
    private PermissionsMapper permissionsMapper;

    /**
     * 新增数据
     *
     * @param permissions 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增权限", notes = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("save")
    public ResponseEntity<Integer> save(Permissions permissions) {
        permissionsMapper.insert(permissions);
        return ResponseEntity.ok(1);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "删除权限", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(Integer id) {
        permissionsMapper.deleteById(id);
        return ResponseEntity.ok(1);
    }

    /**
     * 修改数据
     *
     * @param permissions 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改权限", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PutMapping("update")
    public ResponseEntity<Integer> update(Permissions permissions) {
        permissionsMapper.updateById(permissions);
        return ResponseEntity.ok(1);
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @ApiOperation(value = "查询所有权限", notes = "查询")
    @GetMapping("selectAll")
    public ResponseEntity<List<Permissions>> selectAll() {
        List<Permissions> list = permissionsMapper.selectList(null);
        return ResponseEntity.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "查询单个权限", notes = "查询")
    @GetMapping("selectOne")
    public ResponseEntity<Permissions> selectOne(Integer id) {
        Permissions permissions = permissionsMapper.selectById(id);
        return ResponseEntity.ok(permissions);
    }

}

