package org.test.algorithm;

/**
 * @description:
 * @Title: StringTest
 * @Author xlw
 * @Package org.test.algorithm
 * @Date 2024/3/4 13:37
 */
public class StringTest {

    public static String longestcommonPrefix(String[] arr) {
        String e = arr[0];
        String prefix= null;
        String prefix1 = null;
        boolean b = false;
        for(int i = 1; i <= e.length() - 1; i++) {
            prefix = e.substring(0, i);
            for (String str : arr) {
                b = str.startsWith(prefix);
                if (!b) {
                    return prefix1;
                }
            }
            prefix1 = prefix;
        }
        return prefix1;
    }

    public static boolean isvalid(String s) {
        char start = s.charAt(0);
        char end = s.charAt(s.length() - 1);
        boolean b = false;
        if (b = ('(' == start && ')' == end) || ('[' == start && ']' == end) || ('{' == start && '}' == end)) {
            return b;
        }
        return b;
    }


    public static void main(String[] args) {

        System.out.println(isvalid("([adas])"));
    }

}
