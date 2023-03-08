package com.tianshu.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSONObject;
import com.tianshu.common.annotation.Log;
import com.tianshu.common.constant.UserConstants;
import com.tianshu.common.core.controller.BaseController;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysDept;
import com.tianshu.common.core.domain.entity.SysRole;
import com.tianshu.common.core.domain.entity.SysUser;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.enums.BusinessType;
import com.tianshu.common.utils.AESUtils;
import com.tianshu.common.utils.RandomCodeUtil;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.common.utils.StringUtils;
import com.tianshu.common.utils.poi.ExcelUtil;
import com.tianshu.system.domain.po.RoleDropDown;
import com.tianshu.system.domain.vo.*;
import com.tianshu.system.service.ISysDeptService;
import com.tianshu.system.service.ISysPostService;
import com.tianshu.system.service.ISysRoleService;
import com.tianshu.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    private static final String ORGANIZE_NAME = "天枢易销";

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }



    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
        {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
        {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
//    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (sysUser==null){
            return error("用户不存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(RandomCodeUtil.
                getBackPassword(sysUser.getNickName())));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
//    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
//    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept)
    {
        return success(deptService.selectDeptTreeList(dept));
    }

    /**
     * 用户列表查询
     * @param request
     * @return
     */
    @PostMapping("/query/list")
    public TableDataInfo queryUserList(@Validated @RequestBody QueryUserListVo request){
        return userService.queryUserList(request);
    }

    /**
     * 添加用户
     * @param request
     * @return
     */
    @PostMapping("/add/user")
    public AjaxResult addUser(@Validated @RequestBody AddUserReqVo request){
        return userService.addUser(request);
    }

    /**
     * 用户启用或停用
     * @param userId
     * @param status
     * @return
     */
    @PostMapping("/status/{userId}/{status}")
    public AjaxResult userStatus(@PathVariable("userId") Long userId,@PathVariable("status")Integer status){
        return userService.statusUser(userId,status);
    }

    /**
     * 删除用户
     * @param request
     * @return
     */
    @PostMapping("/delete/user")
    public AjaxResult userDelete(@RequestBody DeleteUserReqVo request){
        return userService.deleteUser(request);
    }

    /**
     * 修改用户信息
     * @param request
     * @return
     */
    @PostMapping("/update/user")
    public AjaxResult userUpdate(@Validated @RequestBody UpdateUserReqVo request){
        return userService.updateUser(request);
    }

    /**
     * 查看用户详情
     * @param userId
     * @return
     */
    @GetMapping("/detail/{userId}")
    public AjaxResult userDetail(@PathVariable("userId") Long userId) throws Exception {
        SysUser sysUser = userService.selectUserDetailById(userId);
        if (BeanUtil.isEmpty(sysUser)){
            return AjaxResult.error("员工不存在");
        }

        HashMap<Object, Object> map = MapUtil.newHashMap();
        map.put("nickName",sysUser.getNickName());
        map.put("phoneNumber",AESUtils.Decrypt(sysUser.getPhonenumber()));
        map.put("sex",sysUser.getSex());
        map.put("dateOfBirth",sf.format(sysUser.getDateOfBirth()));
        map.put("userName",sysUser.getUserName());
        map.put("idCardType",sysUser.getIdCardType());
        map.put("idCardNum", AESUtils.Decrypt(sysUser.getIdCardNum()));
        map.put("companyId",sysUser.getCompanyId());
        map.put("companyName",sysUser.getCompanyName());
        map.put("filialeId",sysUser.getFilialeId());
        map.put("filialeName",sysUser.getFilialeName());
        map.put("deptId",sysUser.getDeptId());
        map.put("deptName",sysUser.getDeptName());
        map.put("deptTree",JSONObject.parseObject(sysUser.getDeptTree().toString()));
        map.put("directSuperior",sysUser.getDirectSuperior());
        map.put("position",sysUser.getPositionName());
        map.put("roleId",sysUser.getRolesId());
        map.put("roleName",sysUser.getRoleName());
        map.put("status",String.valueOf(sysUser.getStatus()));
        return AjaxResult.success(map);
    }

    /**
     * 生成用户账号
     * @param sysUser
     * @return
     */
    @PostMapping("/made/username")
    public AjaxResult getUserNameAcc(@RequestBody SysUser sysUser){

        String userNameAcc = RandomCodeUtil.getUserName(sysUser.getNickName(),ORGANIZE_NAME);
        userNameAcc = userService.madeUserName(userNameAcc);
        HashMap<Object, Object> map = MapUtil.newHashMap();
        map.put("username",userNameAcc);
        return success(map);
    }

    /**
     * 用户下拉列表
     * @return
     */
    @GetMapping("/dorpdown")
    public AjaxResult getUserList(){
        List<DropDownUserList> userList = userService.getUserList();
        return success(userList);
    }

    /**
     * 角色下拉列表
     * @return
     */
    @GetMapping("/role/dropdown")
    public AjaxResult getDropDownRole(){
        List<RoleDropDown> roleDropDown = userService.getRoleDropDown();
        return success(roleDropDown);
    }
}
