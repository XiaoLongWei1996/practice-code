package org.test.design.iterator;

import java.util.Iterator;

/**
 * 迭代器模式:提供一个迭代器类来访问储存顺序
 * @author 肖龙威
 * @date 2021/05/20 14:43
 */
public class DeptIterator implements Iterator<String> {

    private String[] depts = null;

    private int remain;

    private int index = -1;

    public DeptIterator(String[] depts, int remain) {
        this.depts = depts;
        this.remain = remain;
    }

    @Override
    public boolean hasNext() {
        if (index < remain) {
            return true;
        }
        return false;
    }

    @Override
    public String next() {
        if (index > remain) {
            throw new IndexOutOfBoundsException();
        }
        String dept = depts[++index];
        return dept;
    }
}
