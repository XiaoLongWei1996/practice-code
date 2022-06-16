package org.test.design.visitor;

/**
 * @author 肖龙威
 * @date 2021/05/20 15:34
 */
public class AElement implements Element {

    @Override
    public void access(Visitor visitor) {
        visitor.visitor(this);
    }

    public void opera(){
        System.out.println("A元素被操作");
    }
}
