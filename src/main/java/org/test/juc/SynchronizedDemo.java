package org.test.juc;

/**
 * 测试同步关键字Synchronized
 * if会造成虚假唤醒, 应该使用while
 * @author 肖龙威
 * @date 2022/06/23 17:18
 */
public class SynchronizedDemo {

    private int count = 0;

    private static int c1 = 0;

    public void testA() {
        int i = 0;
        synchronized (this) {
            i++;
        }
    }

    public static synchronized void testB() {
        c1++;
    }

    public void testC() {
        synchronized (this) {
            count++;
        }
    }



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

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo demo = new SynchronizedDemo();
//
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                demo.set();
//            }, "ThreadA" + i).start();
//        }
//
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                demo.get();
//            }, "ThreadB" + i).start();
//        }
        Thread thread = new Thread(() -> {
            demo.a();
        });

        thread.start();

        thread.interrupt();


    }

    public void a() {
        int i = 0;
        try {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            while (i < Integer.MAX_VALUE) {
                i++;
            }
            System.out.println(Thread.currentThread().getName() + "结束执行");
        } finally {
            System.out.println("测试线程被打断");
        }
    }
}
