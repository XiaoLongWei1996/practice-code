package org.test.design.visitor;

/**
 * @author 肖龙威
 * @date 2021/05/20 15:35
 */
public class BElement implements Element {

    @Override
    public void access(Visitor visitor) {
        visitor.visitor(this);
    }

    public void opera(){
        System.out.println("B元素被操作");
    }
}
