package com.xlw.test.uploader_demo.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final int FILE_SLICE_SIZE = 1024 * 1024 * 40;

    private static final int FILE_CHUNK_THRESHOLD = 1024 * 1024 * 50;

    private static final String TEMP_DIR = "E:\\Xlw\\temp\\";

    public static String getFileName(String fileName) {
        return RandomUtil.randomString(11) + "." + getFileSuffix(fileName);
    }

    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(FILE_SUFFIX) + 1);
    }

    public static String getMimeType(String fileName) {
        return cn.hutool.core.io.FileUtil.getMimeType(fileName);
    }

    public static String getFileMD5(File file) {
        return SecureUtil.md5(file);
    }

    public static String getFileMD5(InputStream inputStream) {
        return SecureUtil.md5(inputStream);
    }

    @SneakyThrows
    public static List<String> fileSlice(File file) {
        List<String> list = new ArrayList<>();
        try(InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[FILE_SLICE_SIZE];
            int idx = 0;
            int order = 0;
            while((idx = inputStream.read(buffer)) != -1) {
                String chunkName = TEMP_DIR + file.getName() + "." + order;
                OutputStream outputStream = new FileOutputStream(chunkName);
                outputStream.write(buffer, 0, idx);
                outputStream.flush();
                outputStream.close();
                list.add(chunkName);
                order++;
            }
        }
        return list;
    }

    @SneakyThrows
    public static void fileMerge(List<String> chunkList, String fileName) {
        Assert.notEmpty(chunkList, "分片数组不能为空");
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");) {
            byte[] buffer = new byte[FILE_SLICE_SIZE];
            int idx = 0;
            for (String f : chunkList) {
                File file = new File(f);
                InputStream inputStream = new FileInputStream(file);
                while ((idx = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, idx);
                }
                inputStream.close();
            }
        }

    }

    @SneakyThrows(Exception.class)
    public static String getFileMD5(MultipartFile multipartFile) {
        InputStream inputStream = multipartFile.getInputStream();
        try {
            return getFileMD5(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static boolean checkIsChunk(long size) {
        return size > FILE_CHUNK_THRESHOLD;
    }

    public static void main(String[] args) {
        File f = new File("E:\\Xlw\\temp\\nknxbaztfi6.mp4_3");
        String fileMD5 = getFileMD5(f);
        System.out.println(fileMD5);

        //List<String> strings = fileSlice(f);
        //System.out.println(strings);
    }

}
