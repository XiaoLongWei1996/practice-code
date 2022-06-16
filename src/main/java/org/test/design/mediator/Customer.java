package org.test.design.mediator;

/**
 * 客户
 * @author 肖龙威
 * @date 2021/05/20 13:49
 */
public abstract class Customer {

    protected Mediator mediator;

    protected String name;

    protected String role;

    public Customer(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * 发送
     * @param text 发送内容
     */
    public abstract void send(String text);

    /**
     * 接收
     * @param text 接收内容
     */
    public abstract void receive(Customer customer, String text);
}
