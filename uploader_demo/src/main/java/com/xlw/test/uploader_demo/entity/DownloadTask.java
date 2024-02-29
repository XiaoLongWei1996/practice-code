package com.xlw.test.uploader_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 下载任务
 * @Title: DownloadTask
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.entity
 * @Date 2024/2/29 16:01
 */
@Data
@AllArgsConstructor
public class DownloadTask {

    private String fileName;

    private HttpServletResponse response;

}
