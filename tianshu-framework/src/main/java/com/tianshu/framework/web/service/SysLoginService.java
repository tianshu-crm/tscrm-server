package com.tianshu.framework.web.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.tianshu.common.config.AliyunSmsConfig;
import com.tianshu.common.constant.CacheConstants;
import com.tianshu.common.constant.Constants;
import com.tianshu.common.constant.HttpStatus;
import com.tianshu.common.core.domain.entity.SysDept;
import com.tianshu.common.core.domain.entity.SysUser;
import com.tianshu.common.core.domain.model.LoginBody;
import com.tianshu.common.core.domain.model.LoginUser;
import com.tianshu.common.core.redis.RedisCache;
import com.tianshu.common.core.text.Convert;
import com.tianshu.common.exception.ServiceException;
import com.tianshu.common.exception.user.CaptchaException;
import com.tianshu.common.exception.user.CaptchaExpireException;
import com.tianshu.common.exception.user.UserPasswordNotMatchException;
import com.tianshu.common.utils.*;
import com.tianshu.common.utils.ip.IpUtils;
import com.tianshu.framework.manager.AsyncManager;
import com.tianshu.framework.manager.factory.AsyncFactory;
import com.tianshu.framework.security.context.AuthenticationContextHolder;
import com.tianshu.framework.security.sms.SmsCodeAuthenticationToken;
import com.tianshu.system.domain.*;
import com.tianshu.system.service.*;
import com.tianshu.system.domain.vo.SysWechattalkDept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private ISysDingtalkRoleService dingtalkRoleService;

    @Autowired
    private ISysDingtalkUserService dingtalkUserService;

    @Autowired
    private ISysDingtalkDeptService dingtalkDeptService;

    @Autowired
    private FeishuService feishuService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private ISysFeishuDeptService feishuDeptService;

    @Autowired
    private ISysFeishuUserService feishuUserService;

    @Autowired
    private ISysDeptService deptService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }


    public void sendSms(String mobile, String templateCode, Map<String, Object> param) {

        //创建远程连接客户端对象
        DefaultProfile profile = DefaultProfile.getProfile(
                AliyunSmsConfig.REGION_Id,
                AliyunSmsConfig.KEY_ID,
                AliyunSmsConfig.KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        //创建远程连接的请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", AliyunSmsConfig.REGION_Id);
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", AliyunSmsConfig.SIGN_NAME);
        request.putQueryParameter("TemplateCode", AliyunSmsConfig.TEMPLATE_CODE);

        Gson gson = new Gson();
        String json = gson.toJson(param);
        request.putQueryParameter("TemplateParam", json);

        try {
            //使用客户端对象携带请求对象发送请求并得到响应结果
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            HashMap<String, String> resultMap = gson.fromJson(data, HashMap.class);
            String code = resultMap.get("Code");
            String message = resultMap.get("Message");
            log.info("阿里云短信发送响应结果：");
            log.info("code：{}" , code);
            log.info("message：{}" , message);

        } catch (ServerException e) {
            log.error("阿里云短信发送SDK调用失败：");
            log.error("ErrorCode= {}" ,e.getErrCode());
            log.error("ErrorMessage= {}" ,e.getErrMsg());
            throw new ServiceException("阿里云短信发送SDK调用失败", HttpStatus.ERROR);
        } catch (ClientException e) {
            log.error("阿里云短信发送SDK调用失败：");
            log.error("ErrorCode= {}" ,e.getErrCode());
            log.error("ErrorMessage={}", e.getErrMsg());
            throw new ServiceException("阿里云短信发送SDK调用失败", HttpStatus.ERROR);
        }
    }


    /**
     * 短信验证码登录方法
     *
     * @param phonenumber 手机号
     * @param code 验证码
     * @param uuid uuid
     * @return 结果
     */
    public String smsCodeLogin(String phonenumber, String code, String uuid) {
        validateSmsCode(phonenumber, code, uuid); //校验验证码
        Authentication authentication = null; // 用户验证
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new SmsCodeAuthenticationToken(phonenumber));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }


    /**
     * 短信验证码校验
     * @param phonenumber
     * @param code
     * @param uuid
     */
    public void validateSmsCode(String phonenumber, String code, String uuid)
    {
        String verifyKey = CacheConstants.SMS_CODE_KEY+phonenumber;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 钉钉扫二维码登录
     * @param code 授权凭证，一次性使用
     * @return
     */
    public String dingTalkLogin(String code){
        String unionid = dingTalkService.getDingTalkUnionid(code);
        String userId = dingTalkService.getDingUserIdByUnionid(unionid);
        JSONObject userInfo = dingTalkService.getDingUserInfoByUserId(userId);
        String phonenumber = userInfo.get("mobile", String.class);

        if (StringUtils.isNull(phonenumber)){
            throw new ServiceException("无法获取用户手机号");
        }

       /* SysDingtalkUser dingtalkUser = dingtalkUserService.getOne(new LambdaQueryWrapper<SysDingtalkUser>()
                .eq(SysDingtalkUser::getUserid,userId)
        );
        if (StringUtils.isNull(dingtalkUser.getMobile())){
            return unionid;
        }
        String phonenumber = dingtalkUser.getMobile();*/
        Authentication authentication = null; // 用户验证
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new SmsCodeAuthenticationToken(phonenumber));

        } catch (Exception e){
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 获取钉钉组织架构
     */
    @Transactional
    public void getDingtalkOrganizational(){
        //获取钉钉角色信息
        JSON result = dingTalkService.getDingTalkRoleList();
        log.info("钉钉角色信息=============>>>>> {}",result);
        if(StringUtils.equals("ok",result.getByPath("errmsg",String.class))){
            JSON json = JSONUtil.parse(result.getByPath("result", String.class));
            JSONArray jsonArray = JSONUtil.parseArray(json.getByPath("list", String.class));
            log.info("钉钉角色信息=============>>>>>{}",jsonArray);
            List<SysDingtalkRole> sysDingtalkRoles = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSON parse = JSONUtil.parse(obj);
                Long groupId = parse.getByPath("groupId",Long.class);
                JSONArray roles = JSONUtil.parseArray(parse.getByPath("roles", String.class));
                for (Object role : roles) {
                    JSON parseRole = JSONUtil.parse(role);
                    String roleName = parseRole.getByPath("name", String.class);
                    Long roleId = parseRole.getByPath("id", Long.class);
                    SysDingtalkRole sysrole = SysDingtalkRole.builder().roleId(roleId).name(roleName).groupId(groupId).build();
                    sysrole.setCreateInfo(SecurityUtils.getUsername());
                    sysDingtalkRoles.add(sysrole);
                }
            }
            dingtalkRoleService.saveBatch(sysDingtalkRoles);
        }

        //获取钉钉部门以及员工信息
        JSON deptResult = dingTalkService.getDingTalkDeptList();
        log.info("钉钉部门信息=============>>>>>{}",result);
        if(StringUtils.equals("ok",deptResult.getByPath("errmsg",String.class))){
            JSONArray deptArray = JSONUtil.parseArray(deptResult.getByPath("result", String.class));
            List<SysDingtalkDept> sysDingtalkDepts = new ArrayList<SysDingtalkDept>();
            List<SysDingtalkUser> sysDingtalkUsers = new ArrayList<SysDingtalkUser>();
            for (Object obj : deptArray) {
                JSON parseDept = JSONUtil.parse(obj);
                Long parent_id = parseDept.getByPath("parent_id", Long.class);
                String name = parseDept.getByPath("name", String.class);
                Long dept_id = parseDept.getByPath("dept_id", Long.class);
                SysDingtalkDept dingtalkDept = SysDingtalkDept.builder().deptId(dept_id).name(name).parentId(parent_id).build();
                dingtalkDept.setCreateInfo(SecurityUtils.getUsername());
                sysDingtalkDepts.add(dingtalkDept);

                //根据部门id获取用户信息
                JSON dingUserResult = dingTalkService.getDingTalkDetpUserList(dept_id);
                log.info("部门用户信息=============>>>>>{}",dingUserResult);
                if(StringUtils.equals("ok",dingUserResult.getByPath("errmsg",String.class))){
                    JSON userIdLsitJson = JSONUtil.parse(dingUserResult.getByPath("result", String.class));
                    JSONArray userIdArray = JSONUtil.parseArray(userIdLsitJson.getByPath("userid_list", String.class));
                    for (Object o : userIdArray) {
                        String userId = Convert.toStr(o);
                        JSON userInfoRestul = dingTalkService.getDingTalkUserDetail(userId);
                        if(StringUtils.equals("ok",userInfoRestul.getByPath("errmsg",String.class))){
                            log.info("部门用户id信息=============>>>>>{}",userInfoRestul);
                            JSON userInfoJson = JSONUtil.parse(userInfoRestul.getByPath("result", String.class));
                            String unionid = userInfoJson.getByPath("unionid", String.class);
                            String role_list = userInfoJson.getByPath("role_list", String.class);
                            String mobile = userInfoJson.getByPath("mobile", String.class);
                            String avatar = userInfoJson.getByPath("avatar", String.class);
                            String title = userInfoJson.getByPath("title", String.class);
                            String userid = userInfoJson.getByPath("userid", String.class);
                            String userName = userInfoJson.getByPath("name", String.class);
                            String dept_id_list = userInfoJson.getByPath("dept_id_list", String.class);

                            SysDingtalkUser dingtalkUser = SysDingtalkUser.builder().avatar(avatar).name(userName).title(title).deptIdList(dept_id_list).userid(userid)
                                    .unionid(unionid).roleList(role_list).mobile(mobile).build();
                            dingtalkUser.setCreateInfo(SecurityUtils.getUsername());
                            sysDingtalkUsers.add(dingtalkUser);
                        }
                    }
                }
            }
            dingtalkDeptService.saveBatch(sysDingtalkDepts);
            dingtalkUserService.saveBatch(sysDingtalkUsers);
        }

    }

    /**
     * 钉钉手机号登录
     * @param loginBody
     * @return
     */
    public String dingtalkPhoneLogin(LoginBody loginBody){
        return smsCodeLogin(loginBody.getPhonenumber(),loginBody.getCode(),loginBody.getUuid());
    }


    /**
     * 飞书扫二维码登录
     * @param code 登录凭证 一次性使用
     * @return
     */
    public String feishuLogin(String code){
        //获取用户token
        JSONObject data = feishuService.getFeishuLoginUserInfo(code);
        String phonenumber = data.get("mobile", String.class);
        phonenumber = phonenumber.replace("+86", "");
        if(ObjectUtil.isNull(phonenumber)){
            String openId = data.get("openId", String.class);
            return openId;
        }

        Authentication authentication = null; // 用户验证
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new SmsCodeAuthenticationToken(phonenumber));

        } catch (Exception e){
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }


    /**
     * 飞书手机号登录
     * @param loginBody
     * @return
     */
    public String feishuPhoneLogin(LoginBody loginBody){
        return smsCodeLogin(loginBody.getPhonenumber(),loginBody.getCode(),loginBody.getUuid());
    }

    /**
     * 获取飞书组织架构
     */
    @Transactional
    public void getFeishuOrganizational(){
        JSONArray detpList = feishuService.getFeishuDetpList();
        List<SysFeishuDept> feishuDepts = new ArrayList<>();
        List<SysFeishuUser> feishuUsers = new ArrayList<>();
        for (Object obj : detpList) {
            JSONObject jsonDept = JSONUtil.parseObj(obj);
            SysFeishuDept feishuDept = SysFeishuDept.builder()
                    .departmentId(jsonDept.get("open_department_id", String.class))
                    .leaderUserId(jsonDept.get("leader_user_id", String.class))
                    .openDepartmentId(jsonDept.get("open_department_id", String.class))
                    .parentDepartmentId(jsonDept.get("parent_department_id", String.class))
                    .memberCount(jsonDept.get("member_count", Integer.class))
                    .name(jsonDept.get("name", String.class))
                    .build();
            feishuDept.setCreateInfo(SecurityUtils.getUsername());
            feishuDepts.add(feishuDept);
            String open_department_id = jsonDept.get("open_department_id", String.class);
            JSONArray userArray = feishuService.getFeishuDetpUserList(open_department_id);
            for (Object item : userArray) {
                JSONObject jsonUser = JSONUtil.parseObj(item);
                JSONObject avatar = jsonUser.get("avatar", JSONObject.class);
                SysFeishuUser feishuUser = SysFeishuUser.builder()
                        .name(jsonUser.get("name", String.class))
                        .nickname(PinyinUtil.getFirstLetter(jsonUser.get("name", String.class),"")+"@tsyx")
                        .userId(jsonUser.get("user_id", String.class))
                        .unionId(jsonUser.get("union_id", String.class))
                        .openId(jsonUser.get("open_id", String.class))
                        .avatar(avatar.get("avatar_240", String.class))
                        .email(jsonUser.get("email", String.class))
                        .mobile(jsonUser.get("mobile", String.class))
                        .gender(jsonUser.get("gender", Integer.class))
                        .departmentIds(jsonUser.get("department_ids", String.class))
                        .leaderUserId(jsonUser.get("leader_user_id", String.class))
                        .city(jsonUser.get("city", String.class))
                        .country(jsonUser.get("country", String.class))
                        .workStation(jsonUser.get("work_station", String.class))
                        .joinTime(jsonUser.get("join_time", Long.class))
                        .isTenantManager(jsonUser.get("is_tenant_manager", Integer.class))
                        .employeeNo(jsonUser.get("employee_no", String.class))
                        .jobTitle(jsonUser.get("job_title", String.class))
                        .build();
                feishuUser.setCreateInfo(SecurityUtils.getUsername());
                feishuUsers.add(feishuUser);
            }
        }
        feishuDeptService.saveBatch(feishuDepts);
        feishuUserService.saveBatch(feishuUsers);
        //同步系统用户和部门
        //synchSysOrganizational(feishuDepts,feishuUsers);
    }

    private void synchSysOrganizational(List<SysFeishuDept> feishuDepts,List<SysFeishuUser> feishuUsers){
        List<SysUser> userList = new ArrayList<>();
        List<SysDept> deptList = new ArrayList<>();

        for (SysFeishuDept feishuDept : feishuDepts) {
            SysDept sysDept = new SysDept();
            sysDept.setDeptName(feishuDept.getName());
            sysDept.setStatus("0");
            sysDept.setDelFlag("0");
            sysDept.setCreateInfo(SecurityUtils.getUsername());
            sysDept.setEmail(feishuDept.getOpenDepartmentId());//deptid
            sysDept.setAncestors(feishuDept.getParentDepartmentId());//prentid
            sysDept.setLeader(feishuDept.getLeaderUserId());//部门负责人
            deptList.add(sysDept);
        }
        deptService.saveBatch(deptList);

        for (SysDept sysDept : deptList) {
            List<SysDept> parent = deptList.stream().filter(i -> i.getEmail().equals(sysDept.getAncestors())).collect(Collectors.toList());
            sysDept.setParentId(parent.get(0).getDeptId());
            deptService.saveOrUpdate(sysDept);
        }

        for (SysFeishuUser feishuUser : feishuUsers) {
            SysUser sysUser = new SysUser();
            sysUser.setNickName(feishuUser.getName());
            sysUser.setUserName(feishuUser.getNickname());
            sysUser.setSex(feishuUser.getGender().toString());
            sysUser.setPhonenumber(StrUtil.replace(feishuUser.getMobile(),"+86",""));
            sysUser.setEmail(feishuUser.getEmail());
            sysUser.setAvatar(feishuUser.getAvatar());
            sysUser.setCityName(feishuUser.getLeaderUserId());
            sysUser.setCompanyName(feishuUser.getUserId());
            List<SysDept> dept = deptList.stream().filter(i -> i.getDeptId().equals(JSONUtil.parseArray(feishuUser.getDepartmentIds()).getStr(0))).collect(Collectors.toList());
            sysUser.setDeptName(dept.get(0).getDeptName());
            sysUser.setDeptId(dept.get(0).getDeptId());
            sysUser.setCreateInfo(SecurityUtils.getUsername());
            userService.saveBatch(userList);

            //TODO HAO
        }
    }

    /**
     * 企业微信扫二维码登录
     * @param code 登录凭证 一次性使用
     * @return
     */
    public String wxLogin(String code){
        //获取用户token
        String userId = wechatService.getWxUserId(code);
        String phonenumber = wechatService.getWxUserMobile(userId);
        if(ObjectUtil.isNull(phonenumber)){
            return userId;
        }
        Authentication authentication = null; // 用户验证
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new SmsCodeAuthenticationToken(phonenumber));

        } catch (Exception e){
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 企业微信手机号登录
     * @param loginBody
     * @return
     */
    public String wxPhoneLogin(LoginBody loginBody){
        return smsCodeLogin(loginBody.getPhonenumber(),loginBody.getCode(),loginBody.getUuid());
    }

    /**
     * 账号密码获取用户手机号
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String getUserMobile(String username, String password)
    {
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户手机号
        return loginUser.getUser().getPhonenumber();
    }


    @Transactional
    public void getWeChatOrganizational(){
        List<SysWechattalkDept> wechattalkDepts = new ArrayList<>();
        List<Map<String, Object>> deptList = wechatService.getDeptList();
        for (Map<String, Object> map : deptList) {
            SysWechattalkDept wechattalkDept = new SysWechattalkDept();
            wechattalkDept.setName(Convert.toStr(map.get("name")));
            wechattalkDept.setParentId(Convert.toLong(map.get("parentid")));
            wechattalkDept.setDepartmentLeader(JSONUtil.parseArray(map.get("department_leader")).toString());
            wechattalkDept.setDeptId(Convert.toLong(map.get("id")));
            wechattalkDepts.add(wechattalkDept);
            //TODO
        }
    }

}
