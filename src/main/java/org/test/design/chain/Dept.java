package org.test.design.chain;

/**
 * 职责链设计模式:将一个请求沿着一条链传递处理
 * @author 肖龙威
 * @date 2021/05/18 11:08
 */
public abstract class Dept {

    private Dept next;

    private String name;

    public Dept(Dept next, String name) {
        this.next = next;
        this.name = name;
    }

    public Dept() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dept getNext() {
        return next;
    }

    public void setNext(Dept next) {
        this.next = next;
    }

    public abstract void handle(Integer count);
}
