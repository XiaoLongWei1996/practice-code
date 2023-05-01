package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/05/27 14:56
 */
public class Sort {

    /**
     * 冒泡排序
     * @param ints
     */
    public static void bubblingSort(int[] ints) {
        for (int i = 1, j = ints.length; i <= j; i++) { //循环次数
            for (int k = 0, l = ints.length - i - 1; k <= l; k++) { //遍历数组
                if (ints[k] > ints[k + 1]) {
                    int tem = ints[k];
                    ints[k] = ints[k + 1];
                    ints[k + 1] = tem;
                }
            }
        }
    }

    /**
     * 选择排序
     * @param ints
     */
    public static void selectSort(int[] ints) {
        for (int i = 0, l = ints.length - 1; i < l; i++) { //遍历前一个数组值
            for (int j = i + 1, k = ints.length - 1; j <= k; j++) { //遍历后一个数组值
                if (ints[i] > ints[j]) {
                    int tem = ints[i];
                    ints[i] = ints[j];
                    ints[j] = tem;
                }
            }
        }
    }

    /**
     * 插入排序
     * @param ints
     */
    public static void insertSort(int[] ints) {
        for (int i = 0, l = ints.length - 1; i <= l; i++) { //遍历数组
            int val = ints[i]; //待插入元素
            int index = i - 1; //插入坐标
            while (index >= 0 && ints[index] > val) { //数值后移
                ints[index + 1] = ints[index];
                index--;
            }
            ints[index + 1] = val;
        }
    }

    /**
     * 希尔排序
     * @param ints
     */
    public static void hillSort(int[] ints) {
        for (int g = ints.length / 2; g > 0; g /= 2) {
            for (int i = g; i <= ints.length - 1; i++) {
                for (int j = i - g; j >= 0; j -= g) {
                    //交换法
                    if (ints[j + g] < ints[j]) {
                        int temp = ints[j];
                        ints[j] = ints[j + g];
                        ints[j + g] = temp;
                    }
                    //位移法
//                    int temp = ints[j];
//                    int index = j;
//                    while (index < i && temp > ints[index + g]) {
//                        ints[index] = ints[index + g];
//                        index += g;
//                    }
//                    ints[index] = temp;
                }
            }
        }
    }

    /**
     * 快速排序
     * @param ints
     */
    public static void quickSort(int[] ints, int start, int end) {
        //递归退出条件
        if (start > end) {
            return;
        }
        int base = ints[start];
        int left = start;
        int right = end;
        while (left != right) {
            while (ints[right] >= base && right > left) {
                right--;
            }

            while (ints[left] <= base && right > left) {
                left++;
            }

            //交换数据
            int temp = ints[left];
            ints[left] = ints[right];
            ints[right] = temp;
        }
        //交换基准数
        ints[start] = ints[left];
        ints[left] = base;
        //处理左边
        quickSort(ints, start, left - 1);
        //处理右边
        quickSort(ints, left + 1, end);
    }

    /**
     * 归并排序(先分再合)
     * @param arr
     * @param left
     * @param right
     */
    public static void mergeSort(int[] arr, int left, int right) {
        //分割数组
        if (left < right) {
            int mid = (left + right) >> 1; //获取中间索引
            mergeSort(arr, left, mid); //分割左边
            mergeSort(arr, mid + 1, right); //分割右边
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int l = left; //左边索引
        int r = mid + 1; //右边索引
        int[] temp = new int[right - left + 1]; //临时数组
        int tempIndex = 0; //插入坐标
        while (l <= mid && r <= right) {   //比较左右两边的数据
            if (arr[l] > arr[r]) {
                temp[tempIndex] = arr[r];
                tempIndex++;
                r++;
            } else {
                temp[tempIndex] = arr[l];
                tempIndex++;
                l++;
            }
        }

        while (l <= mid) {    //处理左边剩余数据
            temp[tempIndex] = arr[l];
            tempIndex++;
            l++;
        }

        while (r <= right) {   //处理右边剩余数据
            temp[tempIndex] = arr[r];
            tempIndex++;
            r++;
        }

        int index = 0;
        while (index < temp.length) {  //写回到原数组
            arr[left + index] = temp[index];
            index++;
        }
    }

    /**
     * 桶排序(基数排序)
     * @param arr
     * @param maxLen
     */
    public static void bucketSort(int[] arr, int maxLen) {
        //创建桶
        int[][] buckets = new int[10][arr.length];
        //记录每一个桶的元素个数
        int[] counts = new int[10];
        for(int i = 0,j = 1; i < maxLen; i++, j *= 10) { //排序个位,十位,百位
            for (int data : arr) {
                int position = data / j % 10;   //计算元素保存的位置(个,十,百位....的数)
                buckets[position][counts[position]] = data;  //向桶内保存元素
                counts[position] += 1;   //该桶的元素个数+1
            }
            int index = 0;
            for (int k = 0; k < counts.length; k++) { //遍历有元素的桶
                int c = counts[k];   //该桶元素的个数
                if (c > 0) {  //有元素
                    for (int l = 0,len = c; l < len; l++) {  //把元素回写到原数组
                        arr[index] = buckets[k][l];
                        index++;
                    }
                }
            }
            counts = new int[10];  //重置桶元素计数数组
        }
    }

    /**
     * 合并两个无序数组为有序数组
     * 运用了双指针排序 + 插入排序(位移排序)
     * @param ints1 数组
     * @param ints2 数组2
     * @return
     */
    @SuppressWarnings("all")
    public static int[] mergeArr(int[] ints1, int[] ints2) {
        int[] temp = new int[ints1.length + ints2.length];  //合并后的临时数组
        int i = 0;  //数组1的角标
        int j = 0;  //数组2的角标
        int index = 0;  //临时数组的角标
        int tempIndex;  //记录位移排序的角标
        int d;          //插入临时数组的数据
        while (true) {  //死循环
            if (i == ints1.length - 1 || j == ints2.length - 1) {  //退出循环条件
                break;       //退出循环
            }
            if (ints1[i] > ints2[j]) {   //双指针比较两数组的值
                d = ints2[j];
                j++;
            } else {
                d = ints1[i];
                i++;
            }
            tempIndex = index;  //记录位移排序的坐标
            while (tempIndex > 0 && temp[tempIndex - 1] > d) {   //位移排序
                temp[tempIndex] = temp[tempIndex - 1];
                tempIndex--;
            }
            temp[tempIndex] = d;
            index++;
        }
        while (i < ints1.length) {   //处理数组1的剩余数据
           d = ints1[i];
            tempIndex = index;
            while (tempIndex > 0 && temp[tempIndex - 1] > d) { //位移排序
                temp[tempIndex] = temp[tempIndex - 1];
                tempIndex--;
            }
            temp[tempIndex] = d;
            index++;
            i++;
        }
        while (j < ints2.length) { //处理数组2的剩余数据
            d = ints2[j];
            tempIndex = index;
            while (tempIndex > 0 && temp[tempIndex - 1] > d) {  //位移排序
                temp[tempIndex] = temp[tempIndex - 1];
                tempIndex--;
            }
            temp[tempIndex] = d;
            index++;
            j++;
        }
        return temp;  //返回临时数组
    }

}
