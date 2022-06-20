package org.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/04/11 13:23
 */
public class Test {

    public int getInt(){
        int i = 1;
        long l = 2;
        return i;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(15);
        list.add(12);
        list.add(99);

        list.sort((o1, o2) -> {
            return o1 - o2;
        });

        System.out.println(list);
    }

}
