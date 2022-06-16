package org.test.design.visitor;

/**
 * @author 肖龙威
 * @date 2021/05/20 15:33
 */
public interface Element {

    void access(Visitor visitor);
}
