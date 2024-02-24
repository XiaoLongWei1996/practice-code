package com.xlw.test.uploader_demo.config.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 文件类型
 * @Title: FileType
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.config.enums
 * @Date 2024/2/24 23:14
 */
@Getter
public enum FileType {

    DOC(1, "文档"),

    IMG(2, "图片"),

    VIDEO(3, "音频"),

    AUDIO(4, "视频"),

    OTHER(5, "其他");

    private Integer code;

    private String desc;

    private static final Map<Integer, List<String>> FILE_TYPE_MAP = new HashMap<>(4);

    static {
        //文件类型后缀
        FILE_TYPE_MAP.put(1, Arrays.asList("pdf", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "csv", "md", "rtf", "wps", "html", "htm", "xml", "json", "yaml", "yml", "js", "css", "scss", "less", "java", "c", "cpp"));
        //图片类型后缀
        FILE_TYPE_MAP.put(2, Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "psd", "ai", "eps", "raw", "cdr", "pcx", "tga", "exif", "fpx", "svg", "webp", "ps", "eps", "pcd"));
        //音频类型后缀
        FILE_TYPE_MAP.put(3, Arrays.asList("mp3", "wav", "wma", "aac", "flac", "ape", "m4a", "ogg", "amr", "3gp", "m4r", "aac", "mp4", "m4a", "m4p", "m4b", "m4r", "m4v", "mpg", "mpeg", "rm", "rmvb", "wmv", "asf", "asx"));
        //视频类型后缀
        FILE_TYPE_MAP.put(4, Arrays.asList("mp4", "avi", "rm", "rmvb", "mkv", "flv", "mpg", "mpeg", "mov", "wmv", "asf", "asx", "mpe", "mp2", "mpa", "m4a", "m4p", "m4b", "m4r"));
    }

    FileType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static int getFileType(String suffix) {
        for (Map.Entry<Integer, List<String>> e : FILE_TYPE_MAP.entrySet()) {
            if (e.getValue().contains(suffix.toLowerCase())) {
                return e.getKey();
            }
        }
        return FileType.OTHER.getCode();
    }
}
