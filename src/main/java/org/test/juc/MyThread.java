package org.test.juc;

/**
 * @author 肖龙威
 * @date 2023/02/09 12:43
 */
public class MyThread extends Thread{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "执行");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "执行");
        MyThread thread = new MyThread();
        thread.start();
    }
}


