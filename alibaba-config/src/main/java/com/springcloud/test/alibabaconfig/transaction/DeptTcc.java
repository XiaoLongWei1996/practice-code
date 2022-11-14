package com.springcloud.test.alibabaconfig.transaction;

import com.springcloud.test.alibabaconfig.entity.Dept;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Tcc事务模式,需要开启@LocalTCC,并且使用@TwoPhaseBusinessAction指定二阶段的代码
 * @author 肖龙威
 * @date 2022/11/14 14:49
 */
@LocalTCC
public interface DeptTcc {

    @TwoPhaseBusinessAction(name = "deptTcc", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
    void save(@BusinessActionContextParameter(paramName = "dept") Dept dept);

    void commit(BusinessActionContext context);

    void rollback(BusinessActionContext context);
}
