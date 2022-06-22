package org.test.design.iterator;

/**
 * @author 肖龙威
 * @date 2021/05/20 14:53
 */
public class ConcreteCompany implements Company {

    private int capacity;

    private String[] depts = null;

    private int index = -1;

    public ConcreteCompany(int capacity){
        this.capacity = capacity;
        this.depts = new String[capacity];
    }

    @Override
    public void add(String dept) {
        if (index >= capacity) {
            throw new IndexOutOfBoundsException();
        }
        depts[++index] = dept;
    }

    @Override
    public void remove(String dept) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index--;
    }

    @Override
    public DeptIterator getIterator() {
        return new DeptIterator(depts, index);
    }
}
