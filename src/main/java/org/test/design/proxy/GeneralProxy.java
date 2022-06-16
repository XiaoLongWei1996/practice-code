package org.test.design.proxy;

import lombok.AllArgsConstructor;

/**
 * 静态代理:传入需要被代理的对象,太单一
 * @author 肖龙威
 * @date 2021/04/29 13:29
 */
@AllArgsConstructor
public class GeneralProxy {

    private Target target;

    public void show(){
        System.out.println("代理类执行");
        target.execute();
    }
}
