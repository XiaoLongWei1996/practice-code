package org.test.reflex;

import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/01/17 16:19
 */
public class Test {

    public String s = "hello";

    public int i = 1;

    public static char c;

    public void run(String s, int i) {
        s = "word";
        i = 2;
    }


    public static void main(String[] args) {


    }

    public static String getFormatByName(String fieleName) {
        Assert.notNull(fieleName, "输入文件名为空");
        int index = fieleName.lastIndexOf(".");
        return fieleName.substring(index);
    }

    public static long intervalMinutes(Date date1, Date date2){
        Instant instant1 = date1.toInstant();
        Instant instant2 = date2.toInstant();
        Duration duration = Duration.between(instant1, instant2);
        return duration.toMinutes();
    }

    public static int zhixing(int i){
        if (i == 1) {
            return i;
        } else {
            return i + zhixing(i - 1);
        }
    }
}
