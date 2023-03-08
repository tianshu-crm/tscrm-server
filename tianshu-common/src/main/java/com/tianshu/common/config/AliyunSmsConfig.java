package com.tianshu.common.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:haoshaobo
 * @create: 2022-12-28 17:49
 * @Description: 阿里云短信服务
 */
@Data
@Component
@ConfigurationProperties(value = "aliyun.sms")
public class AliyunSmsConfig implements InitializingBean {

    @Value("${aliyun.sms.regionId}")
    private String regionId;
    @Value("${aliyun.sms.keyId}")
    private String keyId;
    @Value("${aliyun.sms.keySecret}")
    private String keySecret;
    @Value("${aliyun.sms.templateCode}")
    private String templateCode;
    @Value("${aliyun.sms.signName}")
    private String signName;

    public static String REGION_Id;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String TEMPLATE_CODE;
    public static String SIGN_NAME;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id = regionId;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        TEMPLATE_CODE = templateCode;
        SIGN_NAME = signName;
    }
}

