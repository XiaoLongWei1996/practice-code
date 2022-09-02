package com.common;

/**
 * @author 肖龙威
 * @date 2022/09/02 15:42
 */
public class Test {

    public static void main(String[] args) {
        String fileName = "aaa.jpg";
        String format = FileUtils.formatByFileName(fileName);
        System.out.println(format);
    }
}
