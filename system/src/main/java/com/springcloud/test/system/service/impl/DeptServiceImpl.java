package com.springcloud.test.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.test.system.dao.DeptMapper;
import com.springcloud.test.system.entity.Dept;
import com.springcloud.test.system.service.DeptService;
import org.springframework.stereotype.Service;

/**
 * 部门表(Dept)表服务实现类
 *
 * @author xlw
 * @since 2022-09-06 10:36:59
 */
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}

