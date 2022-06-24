package org.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
        Date date = new Date();
        LocalDate localDate = dateToLocalDate(date, 12);
        LocalDate now = LocalDate.now();
        System.out.println(now.compareTo(localDate));
    }

    private static LocalDate dateToLocalDate(Date date, long l) {
        LocalDate ld = null;
        Instant instant = date.toInstant();
        ld = LocalDate.from(instant.atZone(ZoneId.of("+8")));
        ld = ld.plusMonths(l);
        return ld;
    }

}
