package org.test.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Title: Test001
 * @Author xlw
 * @Package org.test.inner
 * @Date 2023/11/28 16:37
 */
public class Test001 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(45);
        list.add(11);
        list.add(67);
        list.add(23);
        list.add(10);
        list.stream()
                .filter(o ->  o > 10)
                .sorted(Integer::compare)
                .forEach(System.out::println);
    }
}

class Father {

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
