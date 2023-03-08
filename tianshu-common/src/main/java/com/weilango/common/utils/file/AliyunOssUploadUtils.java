package com.weilango.common.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.weilango.common.config.AliyunOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author:haoshaobo
 * @create: 2022-10-28 14:38
 * @Description: 阿里云对象存储上传工具类
 */
@Component
public class AliyunOssUploadUtils {
    private static AliyunOssConfig aliyunOssConfig;


    /**
     * 使用构造方法注入配置信息
     */
    @Autowired
    public AliyunOssUploadUtils(AliyunOssConfig aliyunOssConfig) {
        AliyunOssUploadUtils.aliyunOssConfig = aliyunOssConfig;
    }

    /**
     * 上传文件
     * @param file
     * @return
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file)  {

        // 生成 OSSClient
        OSS ossClient = new OSSClientBuilder().build(aliyunOssConfig.getEndpoint(), aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret());
        // 原始文件名称String originalFilename = file.getOriginalFilename();

        // 编码文件名
        String filePathName = FileUploadUtils.extractFilename(file);
        // 文件路径名称
        filePathName = aliyunOssConfig.getFilehost() + "/" + filePathName;
        try {
            ossClient.putObject(aliyunOssConfig.getBucketName(), filePathName, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return aliyunOssConfig.getUrl() + "/" + filePathName;
    }
}
