package org.test.design.proxy;

import lombok.AllArgsConstructor;

import java.lang.reflect.Proxy;

/**
 * jdk自带的Proxy,实现被代理对象的接口
 * @author 肖龙威
 * @date 2021/04/29 11:45
 */
@AllArgsConstructor
public class MyProxy{

    private final Subject target;

    private final Class[] interfaceClass;

    public Object proxyTarget () {
        Object object = Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaceClass, (proxy1, method, args) -> {
            System.out.println("proxy方法执行前");
            Object o = method.invoke(target, args);
            System.out.println("proxy方法执行后");
            return o;
        });
        return object;
    }

    public Object excute001() {
        System.out.println("excute001");
        return null;
    }

}
