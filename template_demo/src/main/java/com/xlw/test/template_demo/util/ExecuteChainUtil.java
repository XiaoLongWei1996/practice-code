package com.xlw.test.template_demo.util;


import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

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
                clean();
            }
        }

        /**
         * 异步执行
         *
         * @param pool 池
         */
        public void asyncExecute(ExecutorService pool){
            try {
                CompletableFuture<?>[] futures = new CompletableFuture[chains.size()];
                for (int i = 0; i < chains.size(); i++) {
                    //执行
                    CompletableFuture<Void> future = CompletableFuture.runAsync(chains.get(i), pool);
                    futures[i] = future;
                }
                CompletableFuture<Void> future = CompletableFuture.allOf(futures);
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                clean();
            }
        }

        public void asyncExecute(){
            try {
                CompletableFuture<?>[] futures = new CompletableFuture[chains.size()];
                for (int i = 0; i < chains.size(); i++) {
                    //执行
                    CompletableFuture<Void> future = CompletableFuture.runAsync(chains.get(i));
                    futures[i] = future;
                }
                CompletableFuture<Void> future = CompletableFuture.allOf(futures);
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                clean();
            }
        }


        private void clean() {
            chains.clear();
            chains = null;
        }
    }

    public static void main(String[] args) {
        ExecuteChainUtil
                .createExecuteChain()
                .chain(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("1");
                })
                .chain(true, () -> System.out.println("2"))
                .asyncExecute();
    }
}
