package com.springcloud.test.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.test.system.dao.DeptMapper;
import com.springcloud.test.system.entity.Dept;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 部门表(Dept)表控制层
 *
 * @author xlw
 * @since 2022-09-06 10:36:57
 */
@Api(tags = "部门")
@RestController
@RequestMapping("dept")
@ApiResponses({
        @ApiResponse(code = 200, message = "请求成功"),
        @ApiResponse(code = 400, message = "请求参数没填好"),
        @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
})
public class DeptController {
    /**
     * 服务对象
     */
    @Resource
    private DeptMapper deptMapper;

    /**
     * 新增数据
     *
     * @param dept 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增部门", notes = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int", dataTypeClass = int.class)
    })
    @PostMapping("save")
    public ResponseEntity<Integer> save(Dept dept) {
        deptMapper.insert(dept);
        return ResponseEntity.ok(1);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "删除部门", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @DeleteMapping("delete")
    public ResponseEntity<Integer> delete(Integer id) {
        deptMapper.deleteById(id);
        return ResponseEntity.ok(1);
    }

    /**
     * 修改数据
     *
     * @param dept 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改部门", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int", dataTypeClass = int.class)
    })
    @PutMapping("update")
    public ResponseEntity<Integer> update(Dept dept) {
        deptMapper.updateById(dept);
        return ResponseEntity.ok(1);
    }

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @ApiOperation(value = "查询所有部门", notes = "查询")
    @GetMapping("selectAll")
    public ResponseEntity<List<Dept>> selectAll() throws InterruptedException {
        //Thread.sleep(3000);
        List<Dept> list = deptMapper.selectList(null);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "分页查询", notes = "查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = true, dataType = "int", dataTypeClass = int.class),
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", dataTypeClass = String.class)
    })
    @GetMapping("selectByPage")
    public ResponseEntity<Page<Dept>> selectByPage(Integer currentPage, Integer pageSize, String keyword) {
        Page<Dept> page = Page.of(currentPage, pageSize);
        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like("name", keyword)
                    .or()
                    .like("code", keyword);
        }
        wrapper.orderByAsc("id");
        Page<Dept> result = deptMapper.selectPage(page, wrapper);
        return ResponseEntity.ok(result);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "查询单个部门", notes = "查询")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", dataTypeClass = int.class)
    @GetMapping("selectOne")
    public ResponseEntity<Dept> selectOne(Integer id) {
        Dept dept = deptMapper.selectById(id);
        return ResponseEntity.ok(dept);
    }

}

