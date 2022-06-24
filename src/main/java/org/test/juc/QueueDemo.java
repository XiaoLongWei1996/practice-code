package org.test.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 测试阻塞队列
 * @author 肖龙威
 * @date 2022/06/24 9:36
 */
public class QueueDemo {

    public static void main(String[] args) throws Exception {
        //同步队列,没有容量
        //进去一个元素，必须等待取出来之后，才能再往里面放一个元素！
        BlockingQueue<String> queue = new SynchronousQueue();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "添加:A");
                queue.put("A");
                System.out.println(Thread.currentThread().getName() + "添加:B");
                queue.put("B");
                System.out.println(Thread.currentThread().getName() + "添加:C");
                queue.put("C");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "A").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "取出" + queue.take());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "取出" + queue.take());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "取出" + queue.take());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "B").start();

    }

    private static void testQueue() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue(3);
        //非阻塞,添加
        queue.add("A");
        queue.add("B");
        queue.add("C");
        //queue.add("D"); //会爆出异常
        //非阻塞,获取
        queue.remove();
        queue.remove();
        queue.remove();
        //queue.remove(); //爆出异常

        //非阻塞
        queue.offer("A");  //存,返回false
        queue.poll();    //取,返回null

        //阻塞
        queue.put("R");//存
        queue.take(); //取
    }
}
