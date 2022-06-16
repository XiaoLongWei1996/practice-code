package org.test.design.mediator;

/**
 * @author 肖龙威
 * @date 2021/05/20 13:56
 */
public class Seller extends Customer{

    public Seller(String name, String role) {
        super(name, role);
    }

    @Override
    public void send(String text) {
        System.out.println(name + "("+ role +")发送:" + text);
        mediator.relay(this, text);
    }

    @Override
    public void receive(Customer customer, String text) {
        System.out.println(name + "(" + role + ")接收:"+ customer.name + text);
    }
}
