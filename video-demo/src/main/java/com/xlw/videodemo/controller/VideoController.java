package com.xlw.videodemo.controller;

import cn.hutool.core.io.IoUtil;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @description: 视频处理
 * @Title: VideoController
 * @Author xlw
 * @Package com.xlw.videodemo.controller
 * @Date 2023/7/15 17:26
 */
@RestController
@RequestMapping("video")
public class VideoController {

    @Resource
    ResourceLoader resourceLoader;

    @GetMapping("acquireKey")
    public void acquireKey(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader(
                "Content-disposition",
                "attachment; filename=enc.key");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = resourceLoader.getResource("classpath:static/enc.key").getInputStream();
            outputStream = response.getOutputStream();
            IoUtil.copy(inputStream, outputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    @GetMapping("acquireM3U8")
    public ResponseEntity<String> acquireM3U8() throws IOException {
        InputStream inputStream = null;
        byte[] bytes = null;
        String path = "E:\\Xlw\\media\\player.m3u8";
        try {
            inputStream = new FileInputStream(path);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl")).body(new String(bytes, Charset.forName("utf-8")));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @GetMapping("{ts}")
    public void downloadTs(@PathVariable("ts") String ts, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader(
                "Content-disposition",
                "attachment; filename=" + ts);
        String path = "E:\\Xlw\\media\\%s";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] data = new byte[1024 * 1024 * 10];
        try {
            inputStream = new FileInputStream(String.format(path, ts));
            outputStream = response.getOutputStream();
            int i = 0;
            while ((i = inputStream.read(data)) > 0) {
                outputStream.write(data, 0, i);
            }
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @GetMapping("test")
    public String test() {
        int i = 1 / 0;
        return "test";
    }

}
