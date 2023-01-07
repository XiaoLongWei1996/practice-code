package com.test.springboot.config.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 肖龙威
 * @date 2023/01/06 15:50
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(MinioProperties.class)
@Configuration
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        System.out.println(properties);
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
        //设置minioClient客户端超时时间
        minioClient.setTimeout(5000, 5000, 5000);
        return minioClient;
    }

    @Bean
    public MinioTemplate minioTemplate(MinioClient minioClient) {
        return new MinioTemplate(minioClient);
    }
}
