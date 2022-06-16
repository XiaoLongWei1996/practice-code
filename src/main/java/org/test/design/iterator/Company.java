package org.test.design.iterator;

import java.util.Iterator;

/**
 *
 * @author 肖龙威
 * @date 2021/05/20 14:41
 */
public interface Company {

    void add(String dept);

    void remove(String dept);

    Iterator getIterator();
}
