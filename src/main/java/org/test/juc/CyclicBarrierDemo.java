package org.test.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 测试CyclicBarrier(Java中关于线程的计数器),指定一个数值,让一组线程去等待,当等待的线程数 % 该值 = 0时,就会执行指定的线程,这个线程执行结束时
 * 那些等待的线程就会开始向下继续执行了
 * 注意:当等待的线程数 % 该值 != 0时会一直阻塞,这个时候可以使用超时阻塞方法
 * @author 肖龙威
 * @date 2022/06/23 16:19
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6, () -> {
            System.out.println("线程等待继续执行");
        });

        for (int i = 0; i < 12; i++) {
            final int ii = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ":执行:" + cyclicBarrier.getNumberWaiting());
                try {
                    cyclicBarrier.await(); //阻塞等待,等待cyclicBarrier指定的线程执行完毕,继续执行
                    System.out.println(Thread.currentThread().getName() + "执行结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread" + i).start();
        }
    }
}
