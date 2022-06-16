package org.test.design.mediator;

/**
 * 中介者模式:定义一个对象来封装一系列对象间的交互,降低两个对象间交互的耦合
 * 中介
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
