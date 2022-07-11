package org.test.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS的ABA问题,3个线程T1,T2和T3,T1,T2同时要改变变量A->B,但是只能有一个线程能操作成功,另一个线程处于自旋
 * T3这时候把B->A,这时候T2线程继续向下执行,会把A->B,这就是ABA问题
 * @author 肖龙威
 * @date 2022/07/11 9:12
 */
public class ABADemo {

    private static AtomicInteger count = new AtomicInteger(100);

    public static void main(String[] args) {
        new Thread(() -> {
            //第一次消费50
            count.compareAndSet(100, 50);
            System.out.println(count.get());
        }, "T1").start();

        new Thread(() -> {
            //第二次消费50
            try {
                Thread.sleep(1000);
                count.compareAndSet(100, 50);
                System.out.println(count.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2").start();

        new Thread(() -> {
            count.compareAndSet(50, 100);
            //期望值是100
            System.out.println(count.get());
        }, "T3").start();
    }
}
