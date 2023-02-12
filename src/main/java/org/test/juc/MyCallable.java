package org.test.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author 肖龙威
 * @date 2023/02/09 12:53
 */
public class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(3000);
        return "hello world";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<>(new MyCallable());
        Thread thread = new Thread(future);
        thread.start();
        //future.get()会阻塞获取线程执行的结果
        System.out.println(future.get());
    }
}
