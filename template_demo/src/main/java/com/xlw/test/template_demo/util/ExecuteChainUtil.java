package com.xlw.test.template_demo.util;


import java.util.LinkedList;

/**
 * @description: 执行链工具类
 * @Title: ExecuteChainUtil
 * @Author xlw
 * @Package com.xlw.test.template_demo.util
 * @Date 2025/3/4 10:05
 */
public class ExecuteChainUtil {

    /**
     * 创建执行链
     *
     * @return {@link ExecuteChain }
     */
    public static ExecuteChain createExecuteChain() {
        return new ExecuteChain();
    }

    /**
     * 执行链
     *
     * @author xlw
     * @date 2025/03/04
     */
    public static class ExecuteChain {

        /**
         * 链
         */
        private LinkedList<Runnable> chains;

        /**
         * 执行链
         */
        private ExecuteChain() {
            this.chains = new LinkedList<>();
        }

        /**
         * 链
         *
         * @param task 任务
         * @return {@link ExecuteChain }
         */
        public ExecuteChain chain(Runnable task) {
            chains.addLast(task);
            return this;
        }

        /**
         * 链
         *
         * @param condition 条件
         * @param task      任务
         * @return {@link ExecuteChain }
         */
        public ExecuteChain chain(boolean condition, Runnable task) {
            if (condition) {
                chains.addLast(task);
            }
            return this;
        }

        /**
         * 执行
         */
        public void execute() {
            try {
                for (Runnable task : chains) {
                    //执行
                    task.run();
                }
            } finally {
                chains.clear();
                chains = null;
            }
        }
    }

    public static void main(String[] args) {
        ExecuteChainUtil
                .createExecuteChain()
                .chain(() -> System.out.println("1"))
                .chain(2 > 1, () -> System.out.println("2"))
                .execute();
    }
}
