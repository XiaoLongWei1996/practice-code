package com.springcloud.test.system.controller;


import com.springcloud.test.system.dao.DeptMapper;
import com.springcloud.test.system.entity.Dept;
import com.springcloud.test.system.service.DeptService;
import io.swagger.annotations.*;
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
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "Integer")
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
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
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
    @GetMapping("selectAll")
    public ResponseEntity<List<Dept>> selectAll() {
        List<Dept> list = deptMapper.selectList(null);
        return ResponseEntity.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ResponseEntity<Dept> selectOne(Integer id) {
        Dept dept = deptMapper.selectById(id);
        return ResponseEntity.ok(dept);
    }

}

