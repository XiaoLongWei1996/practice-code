package com.xlw.test.uploader_demo.config;

import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description: 分片上传minio客户端
 * @Title: PearlMinioClient
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.config
 * @Date 2024/2/24 16:50
 */
public class PearlMinioClient extends MinioAsyncClient {

    public PearlMinioClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 创建多部分异步上传
     *
     * @param bucketName       bucket名称
     * @param objectName       对象名称
     * @return {@link CompletableFuture}<{@link CreateMultipartUploadResponse}>
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     */
    public CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(String bucketName, String objectName) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.createMultipartUploadAsync(bucketName, null, objectName, null, null);
    }

    /**
     * 分片上传的预签名上传地址  过期时间为1天 put请求url
     *
     * @param bucketName  桶名
     * @param objectName    Oss文件路径
     * @param queryParams 查询参数
     * @return 分片上传的预签名上传地址
     * @author zhengqingya
     * @date 2023/1/5 17:25
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String objectName, Map<String, String> queryParams) {
        return super.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(1, TimeUnit.DAYS)
                        .extraQueryParams(queryParams)
                        .build());
    }

    /**
     * 查询分片
     *
     * @param bucketName       bucket名称
     * @param objectName       对象名称
     * @param maxParts         马克斯部分
     * @param uploadId         上传身份证
     * @return {@link CompletableFuture}<{@link ListPartsResponse}>
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     */
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String objectName, Integer maxParts, String uploadId) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.listPartsAsync(bucketName, null, objectName, maxParts, 0, uploadId, null, null);
    }

    /**
     * 合并分片
     *
     * @param bucketName       bucket名称
     * @param objectName       对象名称
     * @param uploadId         上传身份证
     * @param parts            部分
     * @return {@link CompletableFuture}<{@link ObjectWriteResponse}>
     * @throws InsufficientDataException 数据不足异常
     * @throws InternalException         内部异常
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     */
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName, String objectName, String uploadId, Part[] parts) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.completeMultipartUploadAsync(bucketName, null, objectName, uploadId, parts, null, null);
    }

    /**
     * 手动上传部件
     *
     * @param bucketName  bucket名称
     * @param objectName  对象名称
     * @param inputStream 输入流
     * @param length      长度
     * @param uploadId
     * @param partNumber
     * @return {@link CompletableFuture}<{@link UploadPartResponse}>
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     */
    public CompletableFuture<UploadPartResponse> uploadPartAsync(String bucketName, String objectName, InputStream inputStream, long length, String uploadId, int partNumber) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.uploadPartAsync(bucketName, null, objectName, inputStream, length, uploadId, partNumber, null, null);
    }

    /**
     * 构建url,只能访问public的bucket
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link HttpUrl}
     * @throws NoSuchAlgorithmException 没有这样算法例外
     */
    public HttpUrl buildUrl(String bucketName, String objectName) throws NoSuchAlgorithmException {
        return super.buildUrl(Method.GET, bucketName, objectName, null, null);
    }


}
