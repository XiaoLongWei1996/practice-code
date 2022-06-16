package org.test.design.proxy;

/**
 * @author 肖龙威
 * @date 2021/04/29 11:35
 */
public class Target implements Subject {

    @Override
    public void execute() {
        System.out.println("target执行");
    }
}
