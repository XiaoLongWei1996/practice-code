package com.xlw.test.uploader_demo.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.xlw.test.uploader_demo.config.MinioProperties;
import com.xlw.test.uploader_demo.config.PearlMinioClient;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import io.minio.messages.Part;
import io.minio.messages.Retention;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: minio工具类
 * @Title: MinioUtil
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.util
 * @Date 2024/2/21 15:59
 */
@Component
@EnableConfigurationProperties(MinioProperties.class)
public class MinioUtil {

    private MinioClient minioClient;

    private PearlMinioClient pearlMinioClient;

    @Resource
    private MinioProperties minioProperties;

    @Resource
    private ServletContext servletContext;

    @PostConstruct
    public void init() {
        //普通客户端
        minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        //自定义分片上传客户端
        pearlMinioClient = new PearlMinioClient(MinioAsyncClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build());
    }

    /**
     * 桶存在
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    @SneakyThrows(Exception.class)
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建桶
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    @SneakyThrows(Exception.class)
    public boolean createBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            return false;
        }
        minioClient.makeBucket(MakeBucketArgs
                .builder()
                .bucket(bucketName)
                .build());
        return true;
    }

    /**
     * 获取所有桶
     *
     * @return {@link List}<{@link Bucket}>
     */
    @SneakyThrows(Exception.class)
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 删除桶
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    @SneakyThrows(Exception.class)
    public boolean deleteBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            return false;
        }
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    @SneakyThrows(Exception.class)
    public void upload(InputStream inputStream, String bucketName, String fileName) {
        String mimeType = servletContext.getMimeType(fileName);
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .contentType(mimeType)
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    @SneakyThrows(Exception.class)
    public void upload(MultipartFile multipartFile, String bucketName, String fileName) {
        InputStream inputStream = multipartFile.getInputStream();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .contentType(multipartFile.getContentType())
                    .object(fileName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .build());
        } finally {
            inputStream.close();
        }
    }

    @SneakyThrows(Exception.class)
    public void localUpload(String bucketName, String fileName, String filePath) {
        String mimeType = servletContext.getMimeType(fileName);
        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(bucketName)
                .contentType(mimeType)
                .object(fileName)
                .filename(filePath)
                .build());
    }

    @SneakyThrows(Exception.class)
    public void deleteFile(String bucketName, String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows(Exception.class)
    public void deleteFiles(String bucketName, String... fileName) {
        List<DeleteObject> collect = Arrays.stream(fileName).map(DeleteObject::new).collect(Collectors.toList());
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(collect).build());
    }

    @SneakyThrows(Exception.class)
    public InputStream download(String bucketName, String fileName) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    /**
     * 下载
     *
     * @param bucketName  bucket名称
     * @param fileName    文件名称
     * @param response    响应
     * @param contentType 内容类型
     */
    @SneakyThrows(Exception.class)
    public void download(String bucketName, String fileName, HttpServletResponse response, @Nullable String contentType) {
        StatObjectResponse statObjectResponse = fileMeta(bucketName, fileName);
        response.setContentType(StrUtil.isBlank(contentType) ? statObjectResponse.contentType() : contentType);
        response.setContentLengthLong(statObjectResponse.size());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream inputStream = download(bucketName, fileName);
        OutputStream outputStream = response.getOutputStream();
        try {
            IoUtil.copy(inputStream, outputStream);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @SneakyThrows(Exception.class)
    public void downloadLocal(String bucketName, String fileName, String filePath) {
        minioClient.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).object(fileName).filename(fileName).build());
    }

    @SneakyThrows(Exception.class)
    public String getPresignedUrl(String bucketName, String fileName, int expireTime, TimeUnit timeUnit) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .method(Method.GET)
                .expiry(expireTime, timeUnit)
                .build());
    }

    /**
     * 获取预先指定post表单数据
     *
     * @param bucketName bucket名称
     * @param expireTime 到期时间
     * @return {@link Map}<{@link String},{@link String}>
     */
    @SneakyThrows(Exception.class)
    public Map<String, String> getPresignedPostFormData(String bucketName, String fileName, long expireTime) {
        // 为存储桶创建一个上传策略，过期时间单位为分钟
        PostPolicy postPolicy = new PostPolicy(bucketName, ZonedDateTime.now().plusMinutes(expireTime));
        // 设置一个参数key，值为上传对象的名称
        postPolicy.addEqualsCondition("key", fileName);
        // 添加Content-Type以"image/"开头，表示只能上传照片
        postPolicy.addStartsWithCondition("Content-Type", "image/");
        // 设置上传文件的大小 64kiB to 10MiB.
        postPolicy.addContentLengthRangeCondition(64 * 1024, 10 * 1024 * 1024);
        return minioClient.getPresignedPostFormData(postPolicy);
    }

    @SneakyThrows(Exception.class)
    public void copy(String sourceBucketName, String sourceFileName, String targetBucketName, String targetFileName) {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(targetBucketName)
                .object(targetFileName)
                .source(CopySource
                        .builder()
                        .bucket(sourceBucketName)
                        .object(sourceFileName)
                        .build())
                .build());
    }

    /**
     * 获取文件保留数据
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @return {@link Retention}
     */
    @SneakyThrows(Exception.class)
    public Retention getFileRetention(String bucketName, String fileName) {
        return minioClient.getObjectRetention(
                GetObjectRetentionArgs.builder()
                        .bucket("my-bucketname-in-eu-with-object-lock")
                        .object("k3s-arm64")
                        .build());
    }

    @SneakyThrows(Exception.class)
    public StatObjectResponse fileMeta(String bucketName, String fileName) {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    @SneakyThrows(Exception.class)
    public String createMultipartUploadAsync(String bucketName, String objectName) {
        return pearlMinioClient.createMultipartUploadAsync(bucketName, objectName).get().result().uploadId();
    }

    @SneakyThrows(Exception.class)
    public ObjectWriteResponse completeMultipartUploadAsync(String bucketName, String objectName, String uploadId, Part[] parts) {
        return pearlMinioClient.completeMultipartUploadAsync(bucketName, objectName, uploadId, parts).get();
    }
    @SneakyThrows(Exception.class)
    public List<Part> listPartsAsync(String bucketName, String objectName, Integer maxParts, String uploadId) {
        return pearlMinioClient.listPartsAsync(bucketName, objectName, maxParts, 0, uploadId).get().result().partList();
    }


}
