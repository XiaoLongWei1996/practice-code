package com.common;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/09/02 15:42
 */
public class Test {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        List<List<Integer>> partition = Lists.partition(list, 1);
        System.out.println(partition);
    }
}
