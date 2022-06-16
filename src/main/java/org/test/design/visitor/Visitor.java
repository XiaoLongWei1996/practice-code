package org.test.design.visitor;

/**
 * 访问者模式:将各种不同类型的元素提取出来封装到一个类中,向外提供一个访问接口,访问里面所有元素
 *           每个元素都有一个处理访问的方法,一个访问者有多个访问不同元素的方法供元素里面处理访问
 *           方法使用.
 * @author 肖龙威
 * @date 2021/05/20 15:31
 */
public interface Visitor {

    void visitor(AElement element);

    void visitor(BElement element);
}
