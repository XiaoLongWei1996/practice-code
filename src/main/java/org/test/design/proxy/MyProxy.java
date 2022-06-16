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

    private final Target target;

    private final Class interfaceClass;

    public Object proxyTarget () {
        //Class<? extends Target> clazz = target.getClass();
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}/*clazz.getInterfaces()*/, (proxy1, method, args) -> {
            System.out.println("proxy方法执行前");
            //Object o = method.invoke(target, args);
            System.out.println("proxy方法执行后");
            return this.excute001();
        });
        return object;
    }

    public Object excute001() {
        System.out.println("excute001");
        return null;
    }

}
