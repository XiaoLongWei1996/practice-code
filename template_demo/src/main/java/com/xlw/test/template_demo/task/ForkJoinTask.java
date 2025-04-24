package com.xlw.test.template_demo.task;


import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author xlw
 * @description:
 * @title: ForkJoinTask
 * @package com.xlw.test.template_demo
 * @date 2025/4/24 17:19
 */
@AllArgsConstructor
public class ForkJoinTask extends RecursiveTask<List<Integer>> {

    /**
     * 数据
     */
    private List<Integer> data;

    /**
     * 门槛
     */
    private int threshold;

    /**
     * 开始
     */
    private int start;

    /**
     * 结束
     */
    private int end;

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected List<Integer> compute() {
        System.out.println(Thread.currentThread().getName());
        if (end - start <= threshold) {
            //处理数据
            List<Integer> list = new ArrayList<>();
            for (int i = start; i < end; i++) {
                list.add(data.get(i) * data.get(i));
            }
            return list;
        }
        //拆分
        int mid = (start + end) >> 1;
        ForkJoinTask left = new ForkJoinTask(data, threshold, start, mid);
        ForkJoinTask right = new ForkJoinTask(data, threshold, mid, end);
        //左边fork
        left.fork();
        //右边fork
        right.fork();

        List<Integer> data1 = left.join();
        List<Integer> data2 = right.join();
        data1.addAll(data2);
        return data1;
    }
}
