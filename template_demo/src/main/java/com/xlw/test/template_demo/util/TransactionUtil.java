package com.xlw.test.template_demo.util;

import com.xlw.test.template_demo.cons.TaskNotReturn;
import com.xlw.test.template_demo.cons.TaskReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;

/**
 * @description: 编程事务
 * @Title: TransactionUtil
 * @Author xlw
 * @Package com.sxkj.pay.util
 * @Date 2024/8/9 9:26
 */
@Slf4j
public class TransactionUtil {

    private static PlatformTransactionManager transactionManager;

    private static PlatformTransactionManager getTransactionManager() {
        if (Objects.isNull(transactionManager)) {
            transactionManager = ApplicationContextUtil.getBean(PlatformTransactionManager.class);
        }
        return transactionManager;
    }

    public static void execute(TaskNotReturn task) {
        TransactionStatus status = getTransactionManager().getTransaction(new DefaultTransactionDefinition());
        try {
            task.execute();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("事务执行异常回滚", e);
        }
    }

    public static <R> R execute(TaskReturn<R> task) {
        TransactionStatus status = getTransactionManager().getTransaction(new DefaultTransactionDefinition());
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
