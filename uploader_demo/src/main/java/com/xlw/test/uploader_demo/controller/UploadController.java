package com.xlw.test.uploader_demo.controller;

import com.xlw.test.uploader_demo.config.Result;
import com.xlw.test.uploader_demo.util.FileUtil;
import com.xlw.test.uploader_demo.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 文件上传
 * @Title: UploadController
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.controller
 * @Date 2024/2/21 20:51
 */
@RestController
@RequestMapping("upload")
public class UploadController {


    @Resource
    private MinioUtil minioUtil;

    @Value("${minio.bucket}")
    private String bucketName;

    /**
     * 普通上传
     *
     * @param file 文件
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("t1")
    public Result<String> upload(MultipartFile file) {
        minioUtil.upload(file, bucketName, FileUtil.getFileName(file.getOriginalFilename()));
        return Result.succeed("上传成功");
    }

    @PostMapping("t2")
    public Result<Map<String, Object>> presignedPostFormData(String fileName) {
        return Result.succeed(minioUtil.getPresignedPostFormData(bucketName, fileName, 10));
    }
}
