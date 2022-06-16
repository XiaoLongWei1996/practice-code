package org.test.design.observer;

/**
 * 观察者模式:当一个对象发生改变时,观察该对象的观察者对象也会做出反应
 * @author 肖龙威
 * @date 2021/05/18 16:01
 */
public interface Observer {

    void notice(String update);
}
