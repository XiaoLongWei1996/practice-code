package org.test.juc;

import java.util.concurrent.Semaphore;

/**
 * Semaphore(信号量)测试:指定一个数值的许可证,线程只有获取到许可证之后才可以执行,否则只能等待获取到许可证;最多容纳指定数值的线程执行
 * 注意:如果线程获取许可证,未释放,则后面的线程都会阻塞等到获取
 * @author 肖龙威
 * @date 2022/06/23 16:40
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 10; i++) {
            final int ii = i;
            new Thread(() -> {
                try {
                    semaphore.acquire(); //阻塞获取许可证
                    System.out.println(Thread.currentThread().getName() + ":执行:" + semaphore.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();  //释放许可证
            }, "thread" + i).start();
        }

    }
}
