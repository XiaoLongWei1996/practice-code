package org.test.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 肖龙威
 * @date 2022/06/27 17:02
 */
public class CASDemo {

    //private static AtomicInteger num = new AtomicInteger(200);

    //private static AtomicStampedReference reference = new AtomicStampedReference(1, 1);

    //测试自旋锁
    private AtomicReference<String> reference = new AtomicReference();

    //加锁
    public void lock() {
        while (!reference.compareAndSet(null, Thread.currentThread().getName())) {

        }
        System.out.println(Thread.currentThread().getName() + ":得到锁");
    }

    //解锁
    public void unlock() {
        reference.compareAndSet(Thread.currentThread().getName(), null);
        System.out.println(Thread.currentThread().getName() + ":释放锁");
    }


    public static void main(String[] args) {
        Demo demo = new Demo();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

                demo.getCount();

            }, "Thread-" + i).start();
        }
    }
}

class Demo {
    //锁对象
    private CASDemo lock = new CASDemo();

    private int count = 10;

    public void getCount() {
        lock.lock();
        System.out.println(count--);
        lock.unlock();
    }

}
