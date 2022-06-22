package org.test.design.mediator;

/**
 * 具体客户
 * @author 肖龙威
 * @date 2021/05/20 14:17
 */
public class Buyer extends Customer {

    public Buyer(String name, String role) {
        super(name, role);
    }

    @Override
    public void send(String text) {
        System.out.println(name + "("+ role +")发送:" + text);
        //原本应该是调用seller.receive()
        mediator.relay(this, text); //用中介对象来调用跟该对象相关的对象
    }

    @Override
    public void receive(Customer customer, String text) {
        System.out.println(name + "(" + role + ")接收:"+ customer.name + text);
    }
}
