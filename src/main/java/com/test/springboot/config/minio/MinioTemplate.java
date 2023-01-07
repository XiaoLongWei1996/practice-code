package com.test.springboot.config.minio;

import ch.qos.logback.core.util.CloseUtil;
import cn.hutool.core.io.IoUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * minio模板
 *
 * @author 肖龙威
 * @date 2023/01/06 16:22
 */
@RequiredArgsConstructor
public class MinioTemplate {

    /** minio客户 */
    private final MinioClient minioClient;

    /**
     * 创建桶
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).objectLock(false).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 桶存在
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除桶
     *
     * @param bucketName bucket名称
     */
    public void deleteBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 桶大小
     *
     * @param bucketName bucket名称
     * @return long
     */
    public long bucketSize(String bucketName) {
        long size = 0;
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .build());
        for (Result<Item> result : results) {
            try {
                size += result.get().size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param bucketName  bucket名称
     * @param fileName    文件名称
     * @return {@link String}
     */
    public String uploadFile(InputStream inputStream, String bucketName, String fileName) {
        String result = null;
        try {
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            result = response.etag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName bucket名称
     * @return {@link String}
     */
    public String uploadFile(File file, String bucketName) {
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getName())
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            result = response.etag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param dir        dir
     * @param bucketName bucket名称
     * @return {@link String}
     */
    public String uploadFile(File file, String dir, String bucketName) {
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(dir + file.getName())
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            result = response.etag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName bucket名称
     * @return {@link String}
     */
    public String uploadFile(MultipartFile file, String bucketName) {
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            result = response.etag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
        }
        return result;
    }

    /**
     * 下载文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @param target     目标
     */
    public void downloadFile(String bucketName, String fileName, OutputStream target) {
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            IoUtil.copy(inputStream, target);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(target);
        }
    }

    /**
     * 下载字节数组
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @return {@link byte[]}
     */
    public byte[] downloadAsByteArray(String bucketName, String fileName) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available());
            IoUtil.copy(inputStream, byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            IoUtil.close(inputStream);
            IoUtil.close(byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     */
    public void removeFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     */
    public void batchRemoveFile(String bucketName, @NonNull String... fileName) {
        List<DeleteObject> list = new ArrayList<>(fileName.length);
        for (String fn : fileName) {
            list.add(new DeleteObject(fn));
        }
        try {
            minioClient.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(list)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 共享文件url
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @param expireTime 到期时间
     * @return {@link String}
     */
    public String shareFileUrl(String bucketName, String fileName, int expireTime) {
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(expireTime, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 文件元数据
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @return {@link String}
     */
    public StatObjectResponse fileMetaData(String bucketName, String fileName) {
        StatObjectResponse response = null;
        try {
            response = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


}
