package com.test.springboot.util;

/**
 * 死锁
 * @author 肖龙威
 * @date 2022/04/20 16:30
 */
public class DeathLock {

    private final String A = "A";

    private final String B = "B";

    public void testA(){
        synchronized (A) {
            System.out.println("A锁被" + Thread.currentThread().getName() + "拿到");
            testB();
        }
    }

    public void testB(){
        synchronized (B) {
            System.out.println("B锁被" + Thread.currentThread().getName() + "拿到");
            testA();
        }
    }

}
