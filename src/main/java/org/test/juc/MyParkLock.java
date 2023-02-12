package org.test.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * @author 肖龙威
 * @date 2023/02/10 21:08
 */
public class MyParkLock {

    public void lock() {
        System.out.println(Thread.currentThread().getName() + "加锁");
        LockSupport.park();
    }

    public void unLock(Thread thread) {
        LockSupport.unpark(thread);
        System.out.println(thread.getName() + "释放锁");
    }

    public static void main(String[] args) {
        MyParkLock lock = new MyParkLock();
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "结束执行");
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unLock(thread1);
        }, "thread2");

        thread1.start();
        thread2.start();
    }
}
