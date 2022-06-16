package org.test.design.visitor;

/**
 * @author 肖龙威
 * @date 2021/05/20 15:40
 */
public class ConcreteVisitor implements Visitor {

    @Override
    public void visitor(AElement element) {
        System.out.println("访问者");
        element.opera();
    }

    @Override
    public void visitor(BElement element) {
        System.out.println("访问者");
        element.opera();
    }
}
