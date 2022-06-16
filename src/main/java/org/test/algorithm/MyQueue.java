package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/05/26 13:25
 */
public class MyQueue<T> {

    private final int MAX_SIZE;

    private final Object[] arr;

    private int head = -1;

    private int tail = -1;

    public MyQueue(int maxSize) {
        this.MAX_SIZE = maxSize;
        arr = new Object[maxSize];
    }

    public int capacity(){
        return MAX_SIZE;
    }

    public int size() {
        return tail - head;
    }

    public void add(T t) {
        tail++;
        if (size() > MAX_SIZE) {
            throw new IndexOutOfBoundsException();
        }
        int index = tail % MAX_SIZE;
        arr[index] = t;
    }

    public T pop() {
        head++;
        if (head > tail) {
            head = -1;
            tail = -1;
            throw new IndexOutOfBoundsException();
        }
        int index = head % MAX_SIZE;
        return (T) arr[index];
    }
}
