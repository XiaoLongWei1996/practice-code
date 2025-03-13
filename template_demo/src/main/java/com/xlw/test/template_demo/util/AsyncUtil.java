package com.xlw.test.template_demo.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * @author xlw
 * @description: 异步工具类
 * @title: AsyncUtil
 * @package com.piaoshui.ncp.util
 * @date 2025/3/13 11:08
 */
public class AsyncUtil {

    /**
     * 异步构建器
     *
     * @return {@link Async }
     */
    public static Async asyncBuilder() {
        return new Async();
    }

    /**
     * 异步构建器
     *
     * @param executor 执行者
     * @return {@link Async }
     */
    public static Async asyncBuilder(Executor executor) {
        return new Async(executor);
    }

    public static class Async {

        private final List<Runnable> taskList;

        private Executor executor;

        public Async() {
            taskList =new ArrayList<>();
        }

        public Async(Executor executor) {
            this.taskList = new ArrayList<>();
            this.executor = executor;
        }

        public Async addTask(Runnable task) {
            taskList.add(task);
            return this;
        }

        public Async addTask(boolean condition, Runnable task) {
            if (condition) {
                taskList.add(task);
            }
            return this;
        }

        /**
         * 同步执行
         */
        public void syncExecute() {
            if (isEmpty()) {
                throw new RuntimeException("未找到可执行任务");
            }
            taskList.forEach(Runnable::run);
        }

        public void asyncExecute() {
            if (isEmpty()) {
                throw new RuntimeException("未找到可执行任务");
            }
            CompletableFuture<?>[] tasks = taskList.stream()
                    .map(task -> Objects.isNull(executor) ? CompletableFuture.runAsync(task) : CompletableFuture.runAsync(task, executor))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture<Void> future = CompletableFuture.allOf(tasks);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean isEmpty() {
            return taskList.isEmpty();
        }
    }

    public static void main(String[] args) {
        asyncBuilder()
                .addTask(() -> System.out.println("1"))
                .addTask(() -> System.out.println("2"))
                .asyncExecute();
    }
}
