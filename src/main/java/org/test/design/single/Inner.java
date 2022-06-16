package org.test.design.single;

/**
 * 内部类单例,利用了外部类可以访问内部类的私有变量
 * @author 肖龙威
 * @date 2021/04/19 13:34
 */
public class Inner {

    private Inner(){

    }

    private static class In {
        private static final Inner INNER = new Inner();
    }

    public static Inner getInstance(){
        return In.INNER;
    }
}
