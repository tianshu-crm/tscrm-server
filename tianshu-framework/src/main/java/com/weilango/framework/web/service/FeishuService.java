package com.weilango.framework.web.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.weilango.common.core.redis.RedisCache;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author:haoshaobo
 * @create: 2022-12-29 16:07
 * @Description: 飞书服务
 */
@Component
public class FeishuService {
    private static final Logger log = LoggerFactory.getLogger(FeishuService.class);
    @Value("${feishu.feishuAppId}")
    private String feishuAppId;

    @Value("${feishu.feishuSecret}")
    private String feishuSecret;

    @Value("${feishu.feishuCallBackUrl}")
    private String feishuCallBackUrl;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取飞书AccessToken
     * @return
     */
    public String getFeishuAccessToken(){
        String accessToken = redisCache.getCacheObject("FEISHU_LOGIN_ACCESS_TOKEN");
        if (accessToken==null || "".equals(accessToken)){
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"app_id\":\"cli_a3241a2cbc7c500d\",\"app_secret\":\"cMJ9QD0HneK3W8cVUq3OzbDYZSHNNFCw\"}");
                Request request = new Request.Builder()
                        .url("https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                log.info("获取飞书token========================>>>>> {}",result);
                JSON resultJson  = JSONUtil.parse(result);
                accessToken = resultJson.getByPath("tenant_access_token",String.class);
                redisCache.setCacheObject("FEISHU_LOGIN_ACCESS_TOKEN", accessToken, 3600, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                throw new RuntimeException("获取飞书token失败==================>>>"+e);
            }
        }

        return accessToken;
    }

    /**
     * 扫码登录获取token
     * @param code
     * @return
     */
    public String getLoginAccessToken(String code){
        String accessToken = redisCache.getCacheObject("FEISHU_LOGIN_ACCESS_TOKEN");
        if (accessToken==null || "".equals(accessToken)){
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "grant_type=authorization_code&client_id="+feishuAppId+"&client_secret="+feishuSecret+"&code="+code+"&redirect_uri="+feishuCallBackUrl);
                Request request = new Request.Builder()
                        .url("https://passport.feishu.cn/suite/passport/oauth/token")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                log.info("获取飞书token========================>>>>>"+result);
                JSON resultJson  = JSONUtil.parse(result);
                accessToken = resultJson.getByPath("access_token",String.class);
                redisCache.setCacheObject("FEISHU_LOGIN_ACCESS_TOKEN", accessToken, 3600, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                throw new RuntimeException("获取飞书token失败==================>>>"+e);
            }
        }

        return accessToken;
    }


    /**
     * 获取扫码登录用户信息
     * @param code
     * @return
     */
    public JSONObject getFeishuLoginUserInfo(String code){
        JSONObject data = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://passport.feishu.cn/suite/passport/oauth/userinfo")
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer "+getLoginAccessToken(code))
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            data = JSONUtil.parseObj(result);
            log.info("获取飞书user_access_token========================>>>>>"+data.toString());
        } catch (Exception e) {
            throw new RuntimeException("获取飞书user_access_token失败==================>>>"+e);
        }
        return data;
    }

    /**
     * 生成飞书二维码登录链接
     * @return
     */
    public String getFeishuLoginUrl() {
        String time = String.valueOf(System.currentTimeMillis());//产生一个当前的毫秒
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        stringBuilder
                .append("https://passport.feishu.cn/suite/passport/oauth/authorize?client_id=")
                .append(feishuAppId)
                .append("&redirect_uri=")
                .append(feishuCallBackUrl)
                .append("&response_type=code")
                .append("&state=")
                .append(time);
        try {
            result = stringBuilder.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }



    /**
     * 自建应用获取 tenant_access_token
     * @return tenant_access_token
     */
    public String  getFeishuTenantAccessToken(){
        String accessToken = redisCache.getCacheObject("FEISHU_TENANT_ACCESS_TOKEN");
        if (accessToken==null || "".equals(accessToken)){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"app_id\": \"cli_a3241a2cbc7c500d\",\r\n    \"app_secret\": \"cMJ9QD0HneK3W8cVUq3OzbDYZSHNNFCw\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal")
                    .method("POST", body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                log.info("获取飞书tenant_access_token========================>>>>>{}",result);
                JSON resultJson  = JSONUtil.parse(result);
                accessToken = resultJson.getByPath("tenant_access_token",String.class);
                Integer expire = resultJson.getByPath("expire",Integer.class);
                redisCache.setCacheObject("FEISHU_TENANT_ACCESS_TOKEN", accessToken, expire, TimeUnit.MICROSECONDS);
            } catch (IOException e) {
                throw new RuntimeException("获取飞书tenant_access_token失败==================>>>{}",e);
            }
        }
        return accessToken;
    }

    /**
     * 获取飞书子部门
     * @return
     */
    public void getFeishuChildrenDetp(JSONArray deptArray, String departmentId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("");
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/contact/v3/departments/"+departmentId+"/children?department_id_type=open_department_id&page_size=10&user_id_type=open_id")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+getFeishuTenantAccessToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSON resultJson  = JSONUtil.parse(result);
            JSONObject data = resultJson.getByPath("data", JSONObject.class);
            if (ObjectUtil.isNotNull(data.getByPath("items"))) {
                JSONArray items = data.getByPath("items",JSONArray.class);
                deptArray.addAll(items);
                for (Object item : items) {
                    getFeishuChildrenDetp(deptArray,JSONUtil.parseObj(item).get("open_department_id").toString());
                }
            }
            log.info("获取飞书部门信息========================>>>>>{}",data);
        } catch (IOException e) {
            throw new RuntimeException("获取飞书部门信息==================>>>{}",e);
        }
    }

    /**
     * 获取部门信息
     * @return
     */
    public JSONArray  getFeishuDetpList(){
        JSONArray jsonArray = new JSONArray();
        getFeishuChildrenDetp(jsonArray,"0");
        log.info("获取飞书部门信息========================>>>>>{}",jsonArray);
        return jsonArray;
    }

    /**
     * 获取部门用户信息
     * @param departmentId
     */
    public JSONArray getFeishuDetpUserList(String departmentId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("");
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/contact/v3/users/find_by_department?department_id="+departmentId+"&department_id_type=open_department_id&page_size=50&user_id_type=open_id")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+getFeishuTenantAccessToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSON resultJson  = JSONUtil.parse(result);
            JSONObject data = resultJson.getByPath("data", JSONObject.class);
            JSONArray items = data.get("items", JSONArray.class);
            log.info("获取飞书部门用户信息========================>>>>>{}",data);
            return ObjectUtil.isNull(items)?new JSONArray():items;
        } catch (IOException e) {
            throw new RuntimeException("获取飞书部门用户信息==================>>>{}",e);
        }
    }


}
