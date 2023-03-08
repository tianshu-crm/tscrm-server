package com.weilango.framework.web.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.weilango.common.core.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

/**
 * @author:haoshaobo
 * @create: 2022-12-29 16:07
 * @Description: 飞书服务
 */
@Component
public class WechatService {
    private static final Logger log = LoggerFactory.getLogger(WechatService.class);
    @Value("${weixin.wxAgentId}")
    private String wxAgentId;

    @Value("${weixin.wxSecret}")
    private String wxSecret;

    @Value("${weixin.wxCallBackUrl}")
    private String wxCallBackUrl;

    @Value("${weixin.wxCorpid}")
    private String wxCorpid;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取飞书AccessToken
     *
     * @return
     */
    public String getWxAccessToken() {
        String accessToken = redisCache.getCacheObject("WECHAT_LOGIN_ACCESS_TOKEN");
        if (accessToken == null || "".equals(accessToken)) {
            HttpResponse res = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + wxCorpid + "&corpsecret=" + wxSecret).execute();
            JSON resJson = JSONUtil.parse(res.body());
            if ("ok".equals(resJson.getByPath("errmsg"))) {
                accessToken = resJson.getByPath("access_token", String.class);
                redisCache.setCacheObject("WECHAT_LOGIN_ACCESS_TOKEN", accessToken, 3600, TimeUnit.MICROSECONDS);
            } else {
                log.info("获取企业微信accessToken失败，失败详细信息为:{}", resJson.getByPath("errmsg"));
            }

        }
        log.info("获取企业微信accessToken===================>>>:{}", accessToken);
        return accessToken;
    }

    /**
     * 生成微信二维码登录链接
     *
     * @return
     */
    public String getWxLoginUrl() {
        String time = String.valueOf(System.currentTimeMillis());//产生一个当前的毫秒
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        stringBuilder
                .append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
                .append(wxCorpid)
                .append("&redirect_uri=")
                .append(wxCallBackUrl)
                .append("&response_type=code")
                .append("&scope=snsapi_privateinfo")
                .append("&state=")
                .append(time)
                .append("&agentid=")
                .append(wxAgentId)
                .append("#wechat_redirect");
        try {
            result = stringBuilder.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * 获取企业微信用户信息
     *
     * @param code
     * @return
     */
    public String getWxUserId(String code) {
        HttpResponse res = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + getWxAccessToken() + "&code=" + code).execute();
        String userId = "";
        JSON resJson = JSONUtil.parse(res.body());
        log.info("获取企业微信用户信息===================>>>:{}", resJson.toStringPretty());
        if ("ok".equals(resJson.getByPath("errmsg"))) {
            userId = resJson.getByPath("user_ticket", String.class);
        } else {
            log.info("获取企业微信userId失败，失败详细信息为:{}", resJson.getByPath("errmsg"));
        }
        log.info("获取企业微信userId===================>>>:{}", userId);
        return userId;
    }

    /**
     * 获取企业微信用户手机号
     *
     * @param userId
     * @return
     */
    public String getWxUserMobile(String userId) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("user_ticket", userId);
        HttpResponse res = HttpRequest.post("https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token=" + getWxAccessToken())
                .headerMap(heads, false)
                .body("{\"user_ticket\":\"" + userId + "\"}")
                .execute();
        String mobile = "";
        JSON resJson = JSONUtil.parse(res.body());
        log.info("获取企业微信用户信息===================>>>:{}", resJson.toStringPretty());
        if ("ok".equals(resJson.getByPath("errmsg"))) {
            mobile = resJson.getByPath("mobile", String.class);
        } else {
            log.info("获取企业微信用户手机号失败，失败详细信息为:" + resJson.getByPath("errmsg"));
        }
        log.info("获取企业微信用户手机号===================>>>:{}", mobile);
        return mobile;
    }

    //获取微信access_token
    public String getWxchatAccessToken() {
        String accessToken = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + wxCorpid + "&corpsecret=" + wxSecret)
                .method("GET", body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().toString();
            JSON resJson = JSONUtil.parse(result);
            accessToken = resJson.getByPath("access_token", String.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return accessToken;
    }

    //获取部门列表
    public List<Map<String, Object>> getDeptList() {
        HttpResponse result = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + getWxAccessToken()).execute();
        //创建返回值容器
        List<Map<String, Object>> mapList = new ArrayList<>();
        String resultOjb = result.body();
        log.info(resultOjb);
        JSON resJson = JSONUtil.parseObj(resultOjb);
        JSONArray department = resJson.getByPath("department", JSONArray.class);
        for (Object obj : department) {
            Map<String, Object> resultMap = new HashMap<>();
            JSON deptJson = JSONUtil.parse(obj);
            resultMap.put("id", deptJson.getByPath("id", Long.class));
            resultMap.put("name", deptJson.getByPath("name", String.class));
            resultMap.put("parentId", deptJson.getByPath("parentid", Long.class));
            resultMap.put("order", deptJson.getByPath("order", Long.class));
            resultMap.put("departmentLeader", deptJson.getByPath("department_leader", String.class));
            mapList.add(resultMap);
        }
        log.info(mapList.toString());
        return mapList;
    }
}
