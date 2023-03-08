package com.tianshu.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *@author:haoshaobo
 *@create: 2022-10-28
 *@Description: ${description}
 */
@Component
@ConfigurationProperties(prefix = "aliyunoss")
@Data
public class AliyunOssConfig {
    /**
     * 地域节点
     */
    private String endpoint;

    /**
     * AccessKey
     */
    private String accessKeyId;

    /**
     * AccessKey秘钥
     */
    private String accessKeySecret;

    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * bucket下文件夹的路径
     */
    private String filehost;

    /**
     * 访问域名
     */
    private String url;
}
