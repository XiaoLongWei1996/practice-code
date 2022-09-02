package com.common;

import cn.hutool.core.lang.Assert;

/**
 * @author 肖龙威
 * @date 2022/09/02 15:25
 */
public class FileUtils {

    public static String formatByFileName(String fileName) {
        Assert.notBlank(fileName, "文件名不能为空");
        int index = fileName.lastIndexOf(".");
        Assert.isFalse(index < 0, "文件名不正确");
        String s = fileName.substring(index + 1);
        return s;
    }
}
