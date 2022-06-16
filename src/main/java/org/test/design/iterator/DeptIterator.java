package org.test.design.iterator;

import java.util.Iterator;
import java.util.List;

/**
 * 迭代器模式:提供一个迭代器类来访问储存顺序
 * @author 肖龙威
 * @date 2021/05/20 14:43
 */
public class DeptIterator implements Iterator<String> {

    private List<String> list;

    private int index = -1;

    public DeptIterator(List<String> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        if (index < list.size() -1) {
            return true;
        }
        return false;
    }

    @Override
    public String next() {
        String dept = null;
        if (hasNext()) {
            dept = list.get(++index);
        }
        return dept;
    }
}
