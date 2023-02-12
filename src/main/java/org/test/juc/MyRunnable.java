package org.test.juc;

/**
 * @author 肖龙威
 * @date 2023/02/09 12:49
 */
public class MyRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "执行");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "执行");
        MyRunnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
