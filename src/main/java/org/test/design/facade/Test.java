package org.test.design.facade;

/**
 * @author 肖龙威
 * @date 2022/06/20 16:24
 */
public class Test {

    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.start();
        facade.run();
        facade.close();
    }
}
