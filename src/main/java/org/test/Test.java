package org.test;

import org.test.algorithm.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/04/11 13:23
 */
public class Test {

    public int getInt() {
        int i = 1;
        long l = 2;
        return i;
    }

    public static void main(String[] args) {
        System.out.println("zhix");
        //int ii = 1/0;
        int[] i1 = {1, 3, 2, 76, 45, 67};
        for (int i : i1) {
            System.out.println(i);
        }
        int[] i2 = {4, 7, 34, 55, 66};
        int[] i3 = Sort.mergeArr(i1, i2);
        System.out.println(Arrays.toString(i3));
    }

    private static LocalDate dateToLocalDate(Date date, long l) {
        LocalDate ld = null;
        Instant instant = date.toInstant();
        ld = LocalDate.from(instant.atZone(ZoneId.of("+8")));
        ld = ld.plusMonths(l);
        return ld;
    }

}
