package org.test.design.prototype;

import sun.misc.Contended;

/**
 * @description:
 * @Title: CacheLineTest
 * @Author xlw
 * @Package org.test.design.prototype
 * @Date 2024/7/5 20:13
 */
public class CacheLineTest {

    @Contended
    private static class Padding {
        // 一个long是8个字节，一共7个long
        //public volatile long p1, p2, p3, p4, p5, p6, p7;
        // x变量8个字节
        public volatile long x = 0L;
    }

    public static Padding[] arr = new Padding[2];

    static {
        arr[0] = new Padding();
        arr[1] = new Padding();
    }

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 10000000; i++) {
                arr[0].x = i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 10000000; i++) {
                arr[1].x = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100000);
    }
}
