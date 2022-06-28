package org.test.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试Volatile:
 * 1.保证内存可见性;
 * 2.不保证原子性;
 * 3.防止指令重排.
 * @author 肖龙威
 * @date 2022/06/27 15:39
 */
public class VolatileDemo {

    //private volatile static int num = 0;

    //原子类,操作都是原子操作
    private static AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 2000; i1++) {
                    num.incrementAndGet();
                }
            }).start();
        }

        while (Thread.activeCount() > 2) {
            System.out.println(Thread.activeCount());
            Thread.yield();
        }

        System.out.println(num);
    }
}
