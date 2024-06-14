package com.jingdianjichi.oss.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置管理
 */
public class MinioConfig {

    /**
     * minioURL
     */
    @Value("${minio.url}")
    private String url;

    /**
     * minio账号
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * minio密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;


    /**
     * 构建MinioClient
     */
    @Bean
    public MinioClient getMinioClient(){
        return MinioClient.builder().endpoint(url).credentials(accessKey,secretKey).build();
    }

}
