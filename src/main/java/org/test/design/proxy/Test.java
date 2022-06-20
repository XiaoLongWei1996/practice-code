package org.test.design.proxy;

/**
 * @author 肖龙威
 * @date 2022/06/20 17:14
 */
public class Test {

    public static void main(String[] args) {
        //被代理对象
        Subject target = new Target();
        //静态代理
        GeneralProxy proxy1 = new GeneralProxy(target);
        proxy1.execute();
        //jdk Proxy代理
        Subject proxy2 = (Subject) new MyProxy(target, target.getClass().getInterfaces()).proxyTarget();
        proxy2.execute();
        //cglib字节码增强代理
        Subject proxy3 = (Subject) new CglibProxy(target).cglibProxy();
        proxy3.execute();
    }
}
