package org.test.inner;

/**
 * @description:
 * @Title: Test001
 * @Author xlw
 * @Package org.test.inner
 * @Date 2023/11/28 16:37
 */
public class Test001 {
    public static void main(String[] args) {
        System.out.println(Father.i);
    }
}

class Father {

    static int i = 1;

    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类实例代码块");
    }

    public Father() {
        System.out.println("父类构造器");
    }
}

class Sun extends Father{
    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类实例代码块");
    }

    public Sun() {
        System.out.println("子类构造器");
    }
}
