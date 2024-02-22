package com.xlw.test.uploader_demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * minio属性
 *
 * @description: minio配置属性
 * @Title: MinioProperties
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.config
 * @Date 2024/2/21 16:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 端点
     */
    private String endpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;

    /**
     * 桶
     */
    private String bucket;

}
