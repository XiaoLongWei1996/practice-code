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

    Integer i = 1;

    public void getInt() {
        Integer i = 2;
        this.i = i + 1;

    }

    public static void main(String[] args) {



    }

    private static LocalDate dateToLocalDate(Date date, long l) {
        LocalDate ld = null;
        Instant instant = date.toInstant();
        ld = LocalDate.from(instant.atZone(ZoneId.of("+8")));
        ld = ld.plusMonths(l);
        return ld;
    }


}
