package org.test.design.single;

/**
 * 懒汉式
 * @author 肖龙威
 * @date 2021/04/19 13:22
 */
public class Lazy {

    private volatile static Lazy lazy = null;

    /** 私有化构造器 */
    private Lazy(){

    }

    public static Lazy getInstance(){
        if (lazy == null) {
            synchronized (Lazy.class) {
                if (lazy == null) {
                    lazy = new Lazy();
                }
            }
        }
        return lazy;
    }

}
