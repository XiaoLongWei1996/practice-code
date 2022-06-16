package org.test.design.inner;

/**
 * @author 肖龙威
 * @date 2021/06/01 16:15
 */
public class Student {

    public class ZhangSan {
        public void show() {
            System.out.println("张山");
        }
    }

    public static class Lishi {
        public void show() {
            System.out.println("李四");
        }

        public static void run() {
            System.out.println("李四运行");
        }
    }
}
