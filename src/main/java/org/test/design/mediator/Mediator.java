package org.test.design.mediator;

/**
 * 中介者模式:定义一个中介对象来封装一系列对象间的交互,降低两个对象间交互的耦合
 * 传统方法是A对象调用B对象,C对象.... -> 中介方法A对象调用中介对象,中介对象来调用B对象,C对象.....
 * 降低了A对象和B对象之间的耦合性
 * @author 肖龙威
 * @date 2021/05/20 13:38
 */
public interface Mediator {

    /**
     * 登记
     */
    void registar(Customer customer);

    /**
     * 转发
     * @param customer 转发的客户
     */
    void relay(Customer customer, String text);

}
