package com.xlw.test.uploader_demo.config;

import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

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
     * 查询分片
     *
     * @param bucketName       bucket名称
     * @param objectName       对象名称
     * @param maxParts         马克斯部分
     * @param partNumberMarker 零件编号标记
     * @param uploadId         上传身份证
     * @return {@link CompletableFuture}<{@link ListPartsResponse}>
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     */
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.listPartsAsync(bucketName, objectName, null, maxParts, partNumberMarker, uploadId, null, null);
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
}
