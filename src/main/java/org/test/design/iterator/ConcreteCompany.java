package org.test.design.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2021/05/20 14:53
 */
public class ConcreteCompany implements Company {

    private List<String> depts = new ArrayList<>();

    @Override
    public void add(String dept) {
        depts.add(dept);
    }

    @Override
    public void remove(String dept) {
        depts.remove(dept);
    }

    @Override
    public Iterator getIterator() {
        return new DeptIterator(depts);
    }
}
