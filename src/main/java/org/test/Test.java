package org.test;

import cn.hutool.core.util.RandomUtil;
import org.test.util.excel.ExcelUtil;
import org.test.util.excel.Student;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<Student> data = new ArrayList<Student>();
        data.add(Student.builder().id(RandomUtil.randomNumbers(6)).name("小明").birthday(new Date()).age(12).className("向日葵班").build());
        data.add(Student.builder().id(RandomUtil.randomNumbers(6)).name("小花").birthday(new Date()).age(13).className("向日葵班").build());
        data.add(Student.builder().id(RandomUtil.randomNumbers(6)).name("小艾").birthday(new Date()).age(14).className("玫瑰班").build());
        data.add(Student.builder().id(RandomUtil.randomNumbers(6)).name("小天").birthday(new Date()).age(15).className("玫瑰班").build());
        data.add(Student.builder().id(RandomUtil.randomNumbers(6)).name("小白").birthday(new Date()).age(11).className("向日葵班").build());
        ExcelUtil.exportMultipart(data);
    }

    private static LocalDate dateToLocalDate(Date date, long l) {
        LocalDate ld = null;
        Instant instant = date.toInstant();
        ld = LocalDate.from(instant.atZone(ZoneId.of("+8")));
        ld = ld.plusMonths(l);
        return ld;
    }

    public void test() {

    }

}
