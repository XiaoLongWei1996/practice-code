package org.test.util;

import org.test.design.stream.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 比较器工具类
 * @Title: CompareUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2023/6/26 20:03
 */
public class CompareUtil {


    public static <T> Comparator<T> acquireStrNumComparator(Function<T, String> function) {
        String numRegex = "^(\\d+)(.*)";
        String strRegex = "^([a-zA-Z]+)(.*)";
        Pattern NumPattern = Pattern.compile(numRegex);
        Pattern strPattern = Pattern.compile(strRegex);
        return (Comparator<T> & Serializable) (o1, o2) -> {
            String r1 = function.apply(o1);
            String r2 = function.apply(o2);
            if (r1 == null) {
                return 1;
            }

            if (r2 == null) {
                return -1;
            }
            //字母开头
            Matcher matcher = strPattern.matcher(r1);
            Matcher matcher1 = strPattern.matcher(r2);
            if (matcher.matches() && matcher1.matches()) {
                String s = matcher.group(1);
                String s1 = matcher1.group(1);
                int i = s.compareTo(s1);
                if (i == 0) {
                    String s2 = matcher.group(2);
                    String s3 = matcher1.group(2);
                    return Integer.valueOf(s2).compareTo(Integer.valueOf(s3));
                }
                return i;
            }
            //数字开头
            Matcher matcher2 = NumPattern.matcher(r1);
            Matcher matcher3 = NumPattern.matcher(r2);
            if (matcher2.matches() && matcher3.matches()) {
                String s = matcher.group(1);
                String s1 = matcher1.group(1);
                int i = Integer.valueOf(s).compareTo(Integer.valueOf(s1));
                if (i == 0) {
                    String s2 = matcher.group(2);
                    String s3 = matcher1.group(2);
                    return s2.compareTo(s3);
                }
                return i;
            }

            //非字母数字开头，乱序
            return r1.compareTo(r2);
        };
    }

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        student.setName("A1");
        list.add(student);
        Student student1 = new Student();
        student1.setName("A2");
        list.add(student1);
        Student student2 = new Student();
        student2.setName("A12");
        list.add(student2);
        Student student3 = new Student();
        student3.setName("A4");
        list.add(student3);
        Student student4 = new Student();
        student4.setName("A24");
        list.add(student4);
        Student student5 = new Student();
        student5.setName("A3");
        list.add(student5);
        Student student6 = new Student();
        student6.setName("A21");
        list.add(student6);
        System.out.println(list);
        list.sort(CompareUtil.acquireStrNumComparator(Student::getName));
        System.out.println(list);

    }
}
