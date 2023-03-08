package com.tianshu.framework.web.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.tianshu.common.core.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author:xxxxx
 * @create: 2022-12-29 16:07
 * @Description: 钉钉服务
 */
@Component
public class DingTalkService {
    private static final Logger log = LoggerFactory.getLogger(DingTalkService.class);
    @Value("${dingtalk.dingTalkAppId}")
    private String dingTalkAppId;

    @Value("${dingtalk.dingTalkSecret}")
    private String dingTalkSecret;

    @Value("${dingtalk.dingTalkCallBackUrl}")
    private String dingTalkCallBackUrl;

    @Value("${dingtalk.dingTalkTokenUrl}")
    private String dingTalkTokenUrl;

    @Value("${dingtalk.dingTalkUserInfoUrl}")
    private String dingTalkUserInfoUrl;

    @Autowired
    private RedisCache redisCache;
    /**
     * 获取钉钉 AccessToken
     * @return
     */
    public String getDingTalkAccessToken() {
        String accessToken = redisCache.getCacheObject("DINGTALK_LOGIN_ACCESS_TOKEN");
        if (accessToken==null || "".equals(accessToken)) {
            try {
                DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(dingTalkTokenUrl);
                OapiGettokenRequest request = new OapiGettokenRequest();
                // 填写步骤一创建应用的Appkey
                request.setAppkey(dingTalkAppId);
                // 填写步骤一创建应用的Appsecret
                request.setAppsecret(dingTalkSecret);
                request.setHttpMethod("GET");
                OapiGettokenResponse response = clientDingTalkClient.execute(request);
                if (response.getErrcode() == 0) {
                    redisCache.setCacheObject("DINGTALK_LOGIN_ACCESS_TOKEN", accessToken, 3600, TimeUnit.MICROSECONDS);
                    return response.getAccessToken();
                }
                log.info("获取accessToken失败:{}", response.getMsg());
            } catch (Exception e) {
                log.error("获取accessToken失败:", e);
            }
        }
        return null;
    }


    /**
     * 获取钉钉unionid
     * @param code
     * @return
     */
    public String getDingTalkUnionid(String code)  {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(code);
            OapiSnsGetuserinfoBycodeResponse rsp = client.execute(req, dingTalkAppId ,dingTalkSecret);
            log.info("获取钉钉用户Unionid========================>>>>>{}",rsp.getBody());
            OapiSnsGetuserinfoBycodeResponse.UserInfo userInfo = rsp.getUserInfo();
            if(null != userInfo){
                return  userInfo.getUnionid();
            }
        }catch (Exception e){
            log.error("获取钉钉用户Unionid:=============={}",e);
        }
        return null;
    }

    /**
     * 根据用户unionid 获取用户信息
     * @param unionid
     * @return
     */
    public String getDingUserIdByUnionid(String unionid){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/getbyunionid");
            OapiUserGetbyunionidRequest req = new OapiUserGetbyunionidRequest();
            req.setUnionid(unionid);
            OapiUserGetbyunionidResponse rsp = client.execute(req, getDingTalkAccessToken());
            log.info("获取钉钉UserId========================>>>>>{}",rsp.getBody());
            JSON json = JSONUtil.parse(rsp.getBody());
            JSON result = JSONUtil.parse(json.getByPath("result", String.class));
            String userid = result.getByPath("userid", String.class);
            return  userid;
        } catch (ApiException e) {
            log.info("获取钉钉UserId失败========================>>>>>{}",e);
        }
        return null;
    }

    public JSONObject getDingUserInfoByUserId(String userId){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
            OapiUserGetRequest req = new OapiUserGetRequest();
            req.setUserid(userId);
            req.setHttpMethod("GET");
            OapiUserGetResponse rsp = client.execute(req, getDingTalkAccessToken());
            log.info("获取钉钉用户信息========================>>>>>{}",rsp.getBody());
            JSONObject json = JSONUtil.parseObj(rsp.getBody());
            return json;
        } catch (ApiException e) {
            log.info("获取钉钉用户信息失败========================>>>>>{}",e);
        }
        return null;
    }

    /**
     * 生成钉钉二维码登录链接
     * @return
     */
    public String getDingTalkLoginUrl(){
        String time = String.valueOf(System.currentTimeMillis());//产生一个当前的毫秒
        StringBuilder stringBuilder = new StringBuilder();
        String result="";
        stringBuilder
                .append("https://oapi.dingtalk.com/connect/qrconnect?appid=")
                //.append("https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=")
                .append(dingTalkAppId)//APP_ID
                .append("&response_type=code")
                .append("&scope=")
                .append("snsapi_login")//snsapi_login
                .append("&state=")
                .append(time)
                .append("&redirect_uri=")
                .append(dingTalkCallBackUrl);//回调地址
        try {
            result = stringBuilder.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }


    /**
     * 获取钉钉部门信息
     * @return
     */
    public JSON getDingTalkDeptList(){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            OapiV2DepartmentListsubResponse rsp = client.execute(req, getDingTalkAccessToken());

            log.info("钉钉部门列表==================>>>>>>{}",rsp.getBody());
            JSON json = JSONUtil.parse(rsp.getBody());
            return json;
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取钉钉角色信息
     * @return
     */
    public JSON getDingTalkRoleList(){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/list");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            OapiV2DepartmentListsubResponse rsp = client.execute(req, getDingTalkAccessToken());
            log.info("钉钉角色列表==================>>>>>>{}",rsp.getBody());
            JSON json = JSONUtil.parse(rsp.getBody());
            return json;
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取钉钉部门用户信息
     * @param deptId
     * @return
     */
    public JSON getDingTalkDetpUserList(Long deptId){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
            OapiUserListidRequest req = new OapiUserListidRequest();
            req.setDeptId(deptId);
            OapiUserListidResponse rsp = client.execute(req, getDingTalkAccessToken());
            log.info("获取部门用户列表==================>>>>>>{}",rsp.getBody());
            JSON json = JSONUtil.parse(rsp.getBody());
            return json;
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取钉钉用户详情
     * @param userId
     * @return
     */
    public JSON getDingTalkUserDetail(String userId){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, getDingTalkAccessToken());
            log.info("钉钉用户详情==================>>>>>>{}",rsp.getBody());
            JSON json = JSONUtil.parse(rsp.getBody());
            return json;
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
