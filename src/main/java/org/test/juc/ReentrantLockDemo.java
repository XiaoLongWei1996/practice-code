package org.test.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试ReentrantLock可重入式锁
 *
 * @author 肖龙威
 * @date 2022/06/23 16:52
 */
public class ReentrantLockDemo {

    private int count = 1;

    private Lock lock = new ReentrantLock();

    private Condition c1 = lock.newCondition();

    private Condition c2 = lock.newCondition();

    private Condition c3 = lock.newCondition();

    public void testA() {
        lock.lock();
        testB();
        lock.unlock();
    }

    public void testB() {
        lock.lock();

        lock.unlock();
    }

    public void executeA() {
        lock.lock();
        if (count != 1) {
            try {
                c1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.count = 2;
        System.out.println(Thread.currentThread().getName() + "执行");
        c2.signal();
        lock.unlock();
    }

    public void executeB() {
        lock.lock();
        if (count != 2) {
            try {
                c2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.count = 3;
        System.out.println(Thread.currentThread().getName() + "执行");
        c3.signal();
        lock.unlock();
    }

    public void executeC() {
        lock.lock();
        if (count != 3) {
            try {
                c3.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.count = 1;
        System.out.println(Thread.currentThread().getName() + "执行");
        c1.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                demo.executeA();
            }
        }, "A").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                demo.executeB();
            }
        }, "B").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                demo.executeC();
            }
        }, "C").start();


    }
}
