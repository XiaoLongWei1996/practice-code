package org.test.juc;

/**
 * 测试同步关键字Synchronized
 * if会造成虚假唤醒, 应该使用while
 * @author 肖龙威
 * @date 2022/06/23 17:18
 */
public class SynchronizedDemo {

    private int count = 0;

    private Object lock = new Object();

    public void set() {
        synchronized (lock) {
            while (count > 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println(Thread.currentThread().getName() + "添加数据" + count);
            lock.notifyAll();
        }
    }

    public void get() {
        synchronized (lock) {
            while (count <= 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count--;
            System.out.println(Thread.currentThread().getName() + "获取数据" + count);
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                demo.set();
            }, "ThreadA" + i).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                demo.get();
            }, "ThreadB" + i).start();
        }

    }
}
