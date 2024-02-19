package org.test.algorithm;

/**
 * @description: 自定义array队列
 * @Title: ArrayQueue
 * @Author xlw
 * @Package org.test.algorithm
 * @Date 2024/2/19 15:01
 */
public class ArrayQueue<T> {


    /**
     * 队列容量
     */
    private int capacity;

    /**
     * 元素个数
     */
    private int size;

    /**
     * 头部指针
     */
    private int head;

    /**
     * 尾部指针
     */
    private int tail;

    /**
     * 对象数组
     */
    private Object[] array;

    /**
     * 锁对象
     */
    private Object lock = new Object();

    public ArrayQueue(int capacity)
    {
        this.capacity = capacity;
        this.head = -1;
        this.tail = -1;
        this.array = new Object[capacity];
    }

    /**
     * 添加元素
     *
     * @param t t
     */
    public void add(T t) {
        synchronized (lock) {
            if (isFull()) {
                throw new IndexOutOfBoundsException("队列已满");
            }
            tail++;
            array[tail % capacity] = t;
            size = tail - head;
        }
    }

    /**
     * 删除元素
     *
     * @return {@link T}
     */
    public T remove() {
        synchronized (lock) {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException("队列为空");
            }
            head++;
            size = tail - head;
            return (T) array[head % capacity];
        }
    }

    /**
     * 返回队列元素个数
     *
     * @return int
     */
    public int size() {
        return this.size;
    }

    /**
     * 判断队列是否为空
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * 队列是否满
     *
     * @return boolean
     */
    private boolean isFull() {
        return size() == capacity;
    }

    /**
     * 打印队列
     */
    public void print() {
        if (isEmpty()) {
            throw new NullPointerException("队列为空");
        }
        System.out.print("[");
        int l = head + 1;
        for (int i = l % capacity; l <= tail; l++, i = l % capacity) {
            System.out.print(array[i] + ",");
        }
        System.out.print("]" + "\r\n");
    }

    public static void main(String[] args) {
        ArrayQueue<Integer> queue = new ArrayQueue<>(5);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.remove();
        queue.remove();
        queue.add(4);
        queue.add(5);
        queue.print();
        System.out.println(queue.size());
    }
}
