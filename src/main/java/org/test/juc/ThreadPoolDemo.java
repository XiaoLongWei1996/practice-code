package org.test.juc;

import java.util.concurrent.*;

/**
 * 测试线程池
 * submit()和 execute()的区别:
 * submit()有返回值,execute()没有返回值;
 * submit()既可以执行Runable子类又可以执行Callable子类,execute()只能执行Runable子类
 * @author 肖龙威
 * @date 2022/06/24 16:17
 */
public class ThreadPoolDemo {

    @SuppressWarnings(value = "all")
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                5,
                1L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                //new ThreadPoolExecutor.AbortPolicy(), //如果线程数超出了线程池最大的容量,爆出异常
                //new ThreadPoolExecutor.CallerRunsPolicy() //如果线程数超出了线程池最大的容量,那个线程启动的线程池,那个线程执行
                //new ThreadPoolExecutor.DiscardPolicy()  //如果线程数超出了线程池最大的容量,丢弃任务,不抛出异常
                new ThreadPoolExecutor.DiscardOldestPolicy() //如果线程数超出了线程池最大的容量,丢弃队列中旧的任务
        );
        for (int i = 0; i < 9; i++) {
            executor.execute(() -> {  //每调用一次这个方法,相当于启动一个线程
                System.out.println(Thread.currentThread().getName() + "执行");
            });
        }
        executor.shutdown();
    }

    @SuppressWarnings(value = "all")
    private static void testExecutor() {
        //ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //ExecutorService executor = Executors.newFixedThreadPool(10);
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {  //每调用一次这个方法,相当于启动一个线程
                System.out.println(Thread.currentThread().getName() + "执行");
            });
        }
        executor.shutdown(); //关闭线程池,如果不关闭那么线程池会一直启动着
    }
}
