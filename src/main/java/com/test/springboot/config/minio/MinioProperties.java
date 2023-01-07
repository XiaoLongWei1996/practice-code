package com.test.springboot.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 肖龙威
 * @date 2023/01/06 15:51
 */
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String defaultBucketName;
}
