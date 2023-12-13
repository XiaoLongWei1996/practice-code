package org.test.juc;

import cn.hutool.core.date.StopWatch;

import java.util.concurrent.*;

/**
 * @description: fork/join demo
 * @Title: ForkJoinDemo
 * @Author xlw
 * @Package org.test.juc
 * @Date 2023/11/30 19:45
 */
public class ForkJoinDemo {

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        ForkJoinPool poll = new ForkJoinPool(10);
        ForkJoinTask<Integer> submit = poll.submit(new Task(0, 100000));
        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        poll.shutdown();
        watch.stop();
        System.out.println(watch.getTotal(TimeUnit.MICROSECONDS));
        watch.start();
        int c = 0;
        for (int i = 0; i <= 100000; i++) {
            c += i;
        }
        System.out.println(c);
        watch.stop();
        System.out.println(watch.getTotal(TimeUnit.MICROSECONDS));

    }

    public static class Task extends RecursiveTask<Integer> {

        private final static int THRESHOLD = 1000;

        private int start;

        private int end;

        public Task(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < THRESHOLD) {
                //相加
                int count = 0;
                for(int i = start; i <= end; i++) {
                    count += i;
                }
                return count;
            } else {
                Task task1 = new Task(start, (start + end) / 2);
                task1.fork();
                Task task2 = new Task((start + end) / 2 + 1, end);
                task2.fork();
                return task1.join() + task2.join();
            }
        }
    }
}
