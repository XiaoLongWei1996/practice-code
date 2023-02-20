package org.test.juc;

/**
 * 测试Volatile:
 * 1.保证内存可见性;
 * 2.不保证原子性;
 * 3.防止指令重排.
 * @author 肖龙威
 * @date 2022/06/27 15:39
 */
public class VolatileDemo {

    private int num = 0;

    //原子类,操作都是原子操作
    //private static AtomicInteger num = new AtomicInteger(0);

    int a = 0, b = 0, c = 0, d = 0;

    public void test() {
        new Thread(() -> {
            c = b;
            a = 1;
        }, "A").start();

        new Thread(() -> {
            d = a;
            b = 1;
        }, "B").start();
    }

    public static void main(String[] args) throws InterruptedException {

    }
}
