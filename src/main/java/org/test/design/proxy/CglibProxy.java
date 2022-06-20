package org.test.design.proxy;

import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib:字节码增强的代理,继承被代理类,该代理方法修改的是class字节码文件
 * @author 肖龙威
 * @date 2021/04/29 13:40
 */
@AllArgsConstructor
public class CglibProxy {

    private Subject target;

    public Object cglibProxy() {
        Object o = Enhancer.create(target.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("proxy执行");
                Object invoke = method.invoke(target, objects);
                return invoke;
            }
        });
        return o;
    }


}
