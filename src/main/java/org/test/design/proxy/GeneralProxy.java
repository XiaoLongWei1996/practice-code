package org.test.design.proxy;

import lombok.AllArgsConstructor;

/**
 * 代理模式:在不改变代码的情况下增强代码的功能
 * 静态代理:传入需要被代理的对象,太单一,每创建一个新类型对象都需要创建一个静态代理类
 * @author 肖龙威
 * @date 2021/04/29 13:29
 */
@AllArgsConstructor
public class GeneralProxy implements Subject {

    private Subject target;

    @Override
    public void execute() {
        System.out.println("静态代理方法执行");
        target.execute();
    }
}
