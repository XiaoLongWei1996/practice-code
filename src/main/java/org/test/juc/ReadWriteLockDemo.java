package org.test.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试读写锁,读读共享,写写互斥,读写互斥
 * @author 肖龙威
 * @date 2022/06/24 8:41
 */
public class ReadWriteLockDemo {

    private volatile int count = 0;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void read() {
        readWriteLock.readLock().lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "读取了:" + count);
        count--;
        readWriteLock.readLock().unlock();
    }

    public void write() {
        readWriteLock.writeLock().lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "写下了:" + count);
        count++;
        readWriteLock.writeLock().unlock();
    }

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                readWriteLockDemo.write();
            }, "A" + i).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                readWriteLockDemo.read();
            }, "B" + i).start();
        }

    }
}
