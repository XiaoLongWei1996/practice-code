package com.xlw.test.uploader_demo.util;

import cn.hutool.core.util.RandomUtil;

/**
 * @description: 文件操作类
 * @Title: FileUtil
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.util
 * @Date 2024/2/21 21:07
 */
public class FileUtil {

    private static final String FILE_SEPARATOR = "/";

    private static final String FILE_SUFFIX = ".";

    public static String getFileName(String fileName) {
        return RandomUtil.randomString(11) + fileName.substring(fileName.lastIndexOf(FILE_SUFFIX));
    }
}
