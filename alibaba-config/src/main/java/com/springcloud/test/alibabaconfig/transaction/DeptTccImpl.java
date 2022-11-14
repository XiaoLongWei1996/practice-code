package com.springcloud.test.alibabaconfig.transaction;

import com.springcloud.test.alibabaconfig.dao.DeptMapper;
import com.springcloud.test.alibabaconfig.entity.Dept;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 肖龙威
 * @date 2022/11/14 14:56
 */
@Component
public class DeptTccImpl implements DeptTcc {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public void save(Dept dept) {
        deptMapper.insert(dept);
    }

    @Override
    public void commit(BusinessActionContext context) {
        System.out.println(context.getActionContext());
        Dept dept = new Dept();
        dept.setType(2);
        dept.setId(context.getActionContext("dept", Dept.class).getId());
        deptMapper.updateById(dept);
    }

    @Override
    public void rollback(BusinessActionContext context) {
        System.out.println(context.getActionContext());
        deptMapper.deleteById(Dept.builder().id(context.getActionContext("dept", Dept.class).getId()).build());
    }
}
