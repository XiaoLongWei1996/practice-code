package org.test.design.iterator;

/**
 *
 * @author 肖龙威
 * @date 2021/05/20 14:41
 */
public interface Company {

    void add(String dept);

    void remove(String dept);

    DeptIterator getIterator();
}
