package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/06/09 14:15
 */
public class Query {

    /**
     * 选择查找
     *
     * @param ints
     * @param data
     * @return
     */
    public static int selectQuery(int[] ints, int data) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == data) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 二分法查找,必须是有序数组
     *
     * @param ints
     * @param data
     * @return
     */
    public static int binaryQuery(int[] ints, int left, int right, int data) {
        if (left > right) {
            return -1;
        }
        int middle = (left + right) / 2;
        int e = ints[middle];
        if (data > e) {
            return binaryQuery(ints, middle + 1, right, data);
        } else if (data < e) {
            return binaryQuery(ints, left, middle - 1, data);
        } else {
            return middle;
        }
    }

    /**
     * 插值查找,插值查找算法类似于二分查找，不同的是插值查找每次从自适应 mid 处开始查找。
     * @param ints
     * @param left
     * @param right
     * @param data
     * @return
     */
    public static int insertQuery(int[] ints, int left, int right, int data) {
        if (left > right) {
            return -1;
        }
        int mid = left + (right - left) * (data - ints[left]) / (ints[right] - ints[left]);
        int e = ints[mid];
        if (data > e) {
            return insertQuery(ints, mid + 1, right, data);
        } else if (data < e) {
            return insertQuery(ints, left, mid - 1, data);
        } else {
            return mid;
        }
    }
}
