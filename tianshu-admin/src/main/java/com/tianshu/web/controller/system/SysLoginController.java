package com.tianshu.web.controller.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianshu.common.config.AliyunSmsConfig;
import com.tianshu.common.constant.CacheConstants;
import com.tianshu.common.constant.Constants;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysMenu;
import com.tianshu.common.core.domain.entity.SysUser;
import com.tianshu.common.core.domain.model.LoginBody;
import com.tianshu.common.core.redis.RedisCache;
import com.tianshu.common.core.text.Convert;
import com.tianshu.common.utils.AESUtils;
import com.tianshu.common.utils.RandomUtils;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.common.utils.StringUtils;
import com.tianshu.framework.web.service.*;
import com.tianshu.system.service.ISysMenuService;
import com.tianshu.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private FeishuService feishuService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 账号密码获取用户手机号
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/getUserMobile")
    public AjaxResult getUserMobile(@RequestBody LoginBody loginBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String mobile = loginService.getUserMobile(loginBody.getUsername(), loginBody.getPassword());
        if(ObjectUtil.isEmpty(mobile)){
            return  AjaxResult.error("无法获取用户手机号码");
        }
        ajax.put("mobile", AESUtils.Decrypt(mobile));
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    @ApiOperation("发送短信验证码")
    @GetMapping("/sendMobileCode/{phonenumber}")
    public AjaxResult sendMobileCode(@ApiParam(value = "手机号码", required = true)
                  @PathVariable String phonenumber) {
        //手机号码不能为空
        if(StringUtils.isEmpty(phonenumber))return AjaxResult.error("手机号码不能为空");

        //手机号码是否合法
        if (!Validator.isMobile(phonenumber))return AjaxResult.error("手机号码不合法");

        //生成验证码
        Map<String, Object> map = new HashMap<>();
        String code = RandomUtils.getFourBitRandom();
        log.info("短信登录验证码:==================>>>>>>{}",code);
        map.put("code", code);

        //获取验证码次数存入redis中
        String today= DateUtil.today();
        Object cacheObject = redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber +":"+ today);
        int mobileTimes = 0;
        if(ObjectUtil.isNotNull(cacheObject)){
            mobileTimes = Convert.toInt(cacheObject);
        }
        if(mobileTimes>10){
            return AjaxResult.error("今日获取验证码已超过10次！");
        }
        //发送阿里云短信验证码
        loginService.sendSms(phonenumber, AliyunSmsConfig.TEMPLATE_CODE,map);

        //将验证码存入redis中
        redisCache.setCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber, code, 5, TimeUnit.MINUTES);
        redisCache.setCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber +":"+ today, ++mobileTimes, 1, TimeUnit.DAYS);
        return AjaxResult.success("获取验证码成功！");
    }

    /**
     * 短信验证码登录
     * @param loginBody
     * @return
     */
    @PostMapping("/mobileLogin")
    public AjaxResult mobileLogin(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.smsCodeLogin(loginBody.getPhonenumber(), loginBody.getCode(), loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 获取钉钉登录二维码
     * @return
     */
    @GetMapping("/dingTalkLoginUrl")
    public AjaxResult dingTalkLogin() {
        AjaxResult ajax = AjaxResult.success();
        // 获取登录url
        String url = dingTalkService.getDingTalkLoginUrl();
        ajax.put("url", url);
        return ajax;
    }

    /**
     * 钉钉扫码登录回调方法
     *
     * @param code 扫码返回code
     * @return 结果
     */
    @GetMapping("/dingTalkCallback")
    public AjaxResult dingTalkLogin(@RequestParam("code") String code) {
        // 生成令牌
        AjaxResult ajax = AjaxResult.success();
        String token = null;
        try {
            token = loginService.dingTalkLogin(code);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotNull(token)){
                map.put("userId",token);
            }
            return AjaxResult.error("无法获取用户手机号",map);
        }

        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }


    /**
     * 钉钉手机号登录
     * @param loginBody
     * @return
     */
    @PostMapping("/dingTalkPhoneLogin")
    public AjaxResult dingTalkPhoneLogin(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.dingtalkPhoneLogin(loginBody);
        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }

    /**
     * 获取钉钉部门用户
     *
     * @return 结果
     */
    @GetMapping("/getDingtalkOrganizational")
    public AjaxResult getDingtalkOrganizational() {
        loginService.getDingtalkOrganizational();
        return AjaxResult.success();
    }


    /**
     * 获取飞书登录二维码
     * @return
     */
    @GetMapping("/feishuLoginUrl")
    public AjaxResult feishuLoginUrl() {
        AjaxResult ajax = AjaxResult.success();
        // 获取登录url
        String url = feishuService.getFeishuLoginUrl();
        ajax.put("url", url);
        return ajax;
    }


    /**
     * 飞书扫码登录回调方法
     *
     * @param code 扫码返回code
     * @return 结果
     */
    @GetMapping("/feishuCallback")
    public AjaxResult feishuCallback(@RequestParam("code") String code) {
        // 生成令牌
        AjaxResult ajax = AjaxResult.success();
        String token = null;
        try {
            token = loginService.feishuLogin(code);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotNull(token)){
                map.put("userId",token);
            }
            return AjaxResult.error("无法获取用户手机号",map);
        }

        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }

    /**
     * 飞书手机号登录
     * @param loginBody
     * @return
     */
    @PostMapping("/feishuPhoneLogin")
    public AjaxResult feishuPhoneLogin(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.feishuPhoneLogin(loginBody);
        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }

    /**
     * 获取飞书部门用户
     *
     * @return 结果
     */
    @GetMapping("/getFeishuOrganizational")
    public AjaxResult getFeishuOrganizational() {
        loginService.getFeishuOrganizational();
        return AjaxResult.success();
    }

    /**
     * 获取企业微信登录二维码
     * @return
     */
    @GetMapping("/wxLoginUrl")
    public AjaxResult wxLoginUrl() {
        AjaxResult ajax = AjaxResult.success();
        // 获取登录url
        String url = wechatService.getWxLoginUrl();
        ajax.put("url", url);
        return ajax;
    }


    /**
     * 企业微信扫码登录回调方法
     *
     * @param code 扫码返回code
     * @return 结果
     */
    @GetMapping("/wxCallback")
    public AjaxResult wxCallback(@RequestParam("code") String code) {
        // 生成令牌
        AjaxResult ajax = AjaxResult.success();
        String token = null;
        try {
            token = loginService.wxLogin(code);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotNull(token)){
                map.put("userId",token);
            }
            return AjaxResult.error("无法获取用户手机号",map);
        }

        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }

    /**
     * 企业微信手机号登录
     * @param loginBody
     * @return
     */
    @PostMapping("/wxPhoneLogin")
    public AjaxResult wxPhoneLogin(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.wxPhoneLogin(loginBody);
        if (token != null) {
            ajax.put(Constants.TOKEN, token);
        } else {
            return AjaxResult.error("登陆失败,请联系管理员或账号登陆");
        }
        return ajax;
    }


    /**
     * 通过账号获取手机号码
     * @param username
     * @return
     */
    @GetMapping("/getUserPhoneByAccount")
    public AjaxResult getUserPhoneByAccount(@RequestParam("username") String username) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName,username)
                .eq(SysUser::getDelFlag,0)
        );
        if (sysUser != null&& StrUtil.isNotEmpty(AESUtils.Decrypt(sysUser.getPhonenumber()))) {
            ajax.put("phonenumber", AESUtils.Decrypt(sysUser.getPhonenumber()));
        } else {
            return AjaxResult.error("对不起，未找到对应的账户信息！");
        }
        return ajax;
    }

    /**
     * 重置密码获取验证码
     * @param loginBody
     * @return
     */
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        //二维码校验
        loginService.validateSmsCode(loginBody.getPhonenumber(),loginBody.getCode(),null);
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber,loginBody.getPhonenumber())
                .eq(SysUser::getDelFlag,0)
        );
        sysUser.setPassword(SecurityUtils.encryptPassword(loginBody.getPassword()));
        sysUser.setUpdateInfo(sysUser.getUserName());
        sysUserService.resetPwd(sysUser);
        return ajax;
    }

    /**
     * 修改密码
     * @param loginBody
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/updPwd")
    public AjaxResult updPassword(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String phonenumber = SecurityUtils.getLoginUser().getUser().getPhonenumber();
        //验证码校验
        loginService.validateSmsCode(phonenumber,loginBody.getCode(),null);
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        sysUser.setPassword(SecurityUtils.encryptPassword(loginBody.getPassword()));
        sysUser.setUpdateInfo(sysUser.getUserName());
        sysUserService.updateUser(sysUser);
        return ajax;
    }

    @ApiOperation("发送短信验证码")
    @GetMapping("/sendMobileCode")
    public AjaxResult sendMobileCode() {
        String phonenumber = SecurityUtils.getLoginUser().getUser().getPhonenumber();
        //手机号码不能为空
        if(StringUtils.isEmpty(phonenumber))return AjaxResult.error("手机号码不能为空");

        //手机号码是否合法
        if (!Validator.isMobile(phonenumber))return AjaxResult.error("手机号码不合法");

        //生成验证码
        Map<String, Object> map = new HashMap<>();
        String code = RandomUtils.getFourBitRandom();
        log.info("短信登录验证码:==================>>>>>>{}",code);
        map.put("code", code);

        //获取验证码次数存入redis中
        String today= DateUtil.today();
        Object cacheObject = redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber +":"+ today);
        int mobileTimes = 0;
        if(ObjectUtil.isNotNull(cacheObject)){
            mobileTimes = Convert.toInt(cacheObject);
        }
        if(mobileTimes>10){
            return AjaxResult.success("今日获取验证码已超过10次！");
        }
        //发送阿里云短信验证码
        loginService.sendSms(phonenumber, AliyunSmsConfig.TEMPLATE_CODE,map);

        //将验证码存入redis中
        redisCache.setCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber, code, 5, TimeUnit.MINUTES);
        redisCache.setCacheObject(CacheConstants.SMS_CODE_KEY + phonenumber +":"+ today, ++mobileTimes, 1, TimeUnit.DAYS);
        return AjaxResult.success("获取验证码成功！");
    }

    /**
     * 用户其他服务本服务运行状态监控
     * @return
     */
    @GetMapping("/appCheck")
    public AjaxResult appCheck() {
        return AjaxResult.success();
    }


    @GetMapping("/getWeChatOrganizational")
    public AjaxResult getWeChatOrganizational() {
        loginService.getWeChatOrganizational();
        return AjaxResult.success();
    }

}
