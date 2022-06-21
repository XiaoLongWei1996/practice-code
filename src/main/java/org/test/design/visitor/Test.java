package org.test.design.visitor;

/**
 * @author 肖龙威
 * @date 2022/06/21 16:42
 */
public class Test {

    public static void main(String[] args) {
        //元素集合
        ElementStructure elementStructure = new ElementStructure();
        //添加元素a
        elementStructure.add(new AElement());
        //添加元素b
        elementStructure.add(new BElement());
        //创建访问者
        Visitor visitor = new ConcreteVisitor();
        //遍历执行访问者
        elementStructure.access(visitor);
        //改变访问者,不同的访问者会赋予元素不同的功能;并且不同的元素会执行访问者不同的方法
    }
}
