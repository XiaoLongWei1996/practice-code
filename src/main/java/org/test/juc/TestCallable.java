package org.test.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 创建线程的3种方式:
 * 1.继承Thread类
 * 2.实现Runable接口,没有返回值
 * 3.实现Callable接口,可以有返回值
 * @author 肖龙威
 * @date 2022/06/23 15:00
 */
public class TestCallable {

    class MyCallabel implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Integer count = 0;
            for (int i = 0; i < 10; i++) {
                count += i;
                System.out.println("执行");
            }
            return count;
        }
    }

    public static void main(String[] args) throws Exception {
        TestCallable testCallable = new TestCallable();
        TestCallable.MyCallabel myCallabel = testCallable.new MyCallabel();
        FutureTask<Integer> task = new FutureTask(myCallabel);
        Thread thread = new Thread(task);
        thread.start();
        Integer count = task.get(5, TimeUnit.SECONDS); //阻塞等待值
        System.out.println(count);
    }
}
