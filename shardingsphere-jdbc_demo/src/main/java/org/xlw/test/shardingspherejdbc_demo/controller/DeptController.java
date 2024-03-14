package org.xlw.test.shardingspherejdbc_demo.controller;


import org.springframework.web.bind.annotation.*;
import org.xlw.test.shardingspherejdbc_demo.entity.Dept;
import org.xlw.test.shardingspherejdbc_demo.service.DeptService;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Dept0)表控制层
 *
 * @author xlw
 * @since 2024-03-14 15:16:13
 */
@RestController
@RequestMapping("dept")
public class DeptController {
    /**
     * 服务对象
     */
    @Resource
    private DeptService deptService;


    @PostMapping("save")
    public String save(Dept dept) {
        deptService.save(dept);
        return "ok";
    }

    @GetMapping("all")
    public List<Dept> all() {
        return deptService.list();
    }

    @GetMapping("one/{id}")
    public Dept selectOne(@PathVariable("id") Long id) {
        return deptService.getById(id);
    }
	
}

