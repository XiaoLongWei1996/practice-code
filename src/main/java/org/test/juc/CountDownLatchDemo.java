package org.test.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 测试CountDownLatch(闭锁)类,指定一个值,当多个线程将这个值消耗为0,则程序继续运行,否则阻塞(等待多个线程执行完毕,继续执行主线程)
 * @author 肖龙威
 * @date 2022/06/23 16:02
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 10; i++) {
            final int ii = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ":执行:" + countDownLatch.getCount());
                countDownLatch.countDown();  //值-1
            }, "thread" + i).start();
        }

        countDownLatch.await(); //阻塞等待,值为0,则继续运行(立马执行)
        System.out.println("程序结束");
    }
}
