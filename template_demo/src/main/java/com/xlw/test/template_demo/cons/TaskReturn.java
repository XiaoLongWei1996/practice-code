package com.xlw.test.template_demo.cons;

/**
 * @description: 有返回值的任务
 * @Title: TaskReturn
 * @Author xlw
 * @Package com.sxkj.pay.cons
 * @Date 2024/8/8 14:53
 */
@FunctionalInterface
public interface TaskReturn<R> {

    R execute();
}
