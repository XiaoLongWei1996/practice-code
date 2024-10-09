package com.xlw.test.template_demo.util;

import com.xlw.test.template_demo.cons.TaskNotReturn;
import com.xlw.test.template_demo.cons.TaskReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @description: 编程事务
 * @Title: TransactionUtil
 * @Author xlw
 * @Package com.sxkj.pay.util
 * @Date 2024/8/9 9:26
 */
@Slf4j
@RequiredArgsConstructor
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;

    public boolean execute(TaskNotReturn task) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            task.execute();
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("事务执行异常回滚", e);
            return false;
        }
    }

    public <R> R execute(TaskReturn<R> task) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            R r = task.execute();
            transactionManager.commit(status);
            return r;
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("事务执行异常回滚", e);
            throw e;
        }
    }

}
