package org.test.algorithm;

import java.util.Arrays;

/**
 * @author 肖龙威
 * @date 2022/06/13 16:10
 */
public class ArrayTree {

    private int[] arr;

    int capacity = 0;

    int index = 0;

    public ArrayTree(int capacity) {
        this.arr = new int[capacity];
        this.capacity = capacity;
    }

    public void add(int i) {
        arr[index] = i;
        index++;
    }

    public void deleteAt(int index) {
        int len = capacity - index - 1;
        System.arraycopy(arr, index + 1, arr, index, len);
        arr[--capacity] = 0;
    }

    public void list() {
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 树遍历
     * @param index
     */
    public void tree(int index) {
        System.out.println(arr[index]);
        int left = 2 * index + 1;
        if (left < capacity) {
            tree(left);
        }

        int right = 2 * index + 2;
        if (right < capacity) {
            tree(right);
        }
    }

    public int getParentNode(int index){
        if (index - 1 < 0) {
            return -1;
        }
        return arr[(index - 1) / 2];
    }
}
