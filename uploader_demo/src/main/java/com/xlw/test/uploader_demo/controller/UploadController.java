package com.xlw.test.uploader_demo.controller;

import com.xlw.test.uploader_demo.config.Result;
import com.xlw.test.uploader_demo.entity.FileStore;
import com.xlw.test.uploader_demo.service.FileStoreService;
import com.xlw.test.uploader_demo.util.FileUtil;
import com.xlw.test.uploader_demo.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private FileStoreService fileStoreService;

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

    /**
     * 临时上传文件
     *
     * @param fileName 文件名称
     * @return {@link Result}<{@link Map}<{@link String}, {@link Object}>>
     */
    @PostMapping("t2")
    public Result<Map<String, Object>> presignedPostFormData(String fileName) {
        return Result.succeed(minioUtil.getPresignedPostFormData(bucketName, fileName, 10));
    }

    @GetMapping("presignedUrl/{fileName}")
    public Result<String> presignedUrl(@PathVariable("fileName") String fileName) {
        return Result.succeed(minioUtil.getPresignedUrl(bucketName, fileName, 5, TimeUnit.MINUTES));
    }

    @GetMapping("download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        minioUtil.download(bucketName, fileName, response, null);
    }

    @GetMapping("download1/{fileName}")
    public void download1(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        minioUtil.download(bucketName, fileName, response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    @PostMapping("getFileMD5")
    public Result<String> getFileMD5(MultipartFile file) {
        return Result.succeed(FileUtil.getFileMD5(file));
    }

    @PostMapping("multipartUpload")
    public Result<String> multipartUpload(String fileName) {
        return Result.succeed(null);
    }

    @GetMapping("mergeFile")
    public Result<String> mergeFile(String fileName) {
        return Result.succeed(null);
    }

    @PostMapping("createMultipartUpload")
    public Result<String> createMultipartUpload(FileStore fileStore) {
        return Result.succeed(fileStoreService.createUploader(fileStore));
    }
}
