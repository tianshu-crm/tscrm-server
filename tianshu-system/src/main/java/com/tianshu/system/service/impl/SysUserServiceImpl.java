package com.tianshu.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.annotation.DataScope;
import com.tianshu.common.constant.UserConstants;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysDept;
import com.tianshu.common.core.domain.entity.SysRole;
import com.tianshu.common.core.domain.entity.SysUser;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.exception.ServiceException;
import com.tianshu.common.utils.AESUtils;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.common.utils.StringUtils;
import com.tianshu.common.utils.bean.BeanValidators;
import com.tianshu.common.utils.spring.SpringUtils;
import com.tianshu.system.domain.SysPost;
import com.tianshu.system.domain.SysUserPost;
import com.tianshu.system.domain.SysUserRole;
import com.tianshu.system.domain.vo.*;
import com.tianshu.system.mapper.*;
import com.weilango.system.domain.*;
import com.tianshu.system.domain.po.RoleDropDown;
import com.weilango.system.domain.vo.*;
import com.weilango.system.mapper.*;
import com.tianshu.system.service.ISysConfigService;
import com.tianshu.system.service.ISysUserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private SysFilialeMapper sysFilialeMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    protected Validator validator;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SimpleDateFormat simpleDateFormatymd = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(user);
                    checkUserDataScope(user.getUserId());
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param phone
     * @return
     */
    @Override
    public SysUser selectUserByPhone(String phone) throws Exception {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public TableDataInfo queryUserList(QueryUserListVo request) {
        log.info("员工列表查询接口入参:{}", JSON.toJSONString(request));
        //创建查询
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(request.getNickName())) {
            queryWrapper.eq(SysUser::getNickName, request.getNickName());
        }
        if (StringUtils.isNotBlank(request.getPhoneNumber())) {
            queryWrapper.eq(SysUser::getPhonenumber, request.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(request.getCompanyName())) {
            queryWrapper.like(SysUser::getCompanyName, request.getCompanyName());
        }
        if (StringUtils.isNotBlank(request.getFilialeName())) {
            queryWrapper.like(SysUser::getFilialeName, request.getFilialeName());
        }
        if (StringUtils.isNotBlank(request.getDeptName())) {
            queryWrapper.like(SysUser::getDeptName, request.getDeptName());
        }
        if (StringUtils.isNotBlank(request.getPositionName())) {
            queryWrapper.eq(SysUser::getPositionName, request.getPositionName());
        }
        queryWrapper.eq(SysUser::getDelFlag, 0);
        queryWrapper.ne(SysUser::getUserId,1L);
        queryWrapper.orderByDesc(SysUser::getCreateTime);
        Long total = baseMapper.selectCount(queryWrapper);

        List<QueryUserListRespVo> resultList = new ArrayList<>();

        TableDataInfo tableDataInfo = new TableDataInfo();
        if (total == 0) {
            tableDataInfo.setCode(200);
            tableDataInfo.setMsg("查询成功");
            tableDataInfo.setRows(resultList);
            tableDataInfo.setTotal(total);
            return tableDataInfo;
        }
        Page<SysUser> page = new Page<>();
        page.setCurrent(request.getPageNum());
        page.setSize(request.getPageSize());
        Page<SysUser> sysUserPage = baseMapper.selectPage(page, queryWrapper);
        List<SysUser> records = sysUserPage.getRecords();
        //处理返回的生日
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        tableDataInfo.setRows(buildUserVo(records));
        tableDataInfo.setTotal(total);
        return tableDataInfo;
    }

    @SneakyThrows
    @Override
    public AjaxResult addUser(AddUserReqVo request) {
        log.info("添加员工数据接口入参:{}", JSON.toJSONString(request));

        //构建入库po 城市
        SysUser sysUser = new SysUser();
        sysUser = dealDeptTree(request.getDeptTree(), sysUser);
        if (sysUser == null) {
            return AjaxResult.error("员工不能直属公司,请为员工划分部门");
        }
        JSONObject result = new JSONObject();
        //检查当前的手机号是否已经重复了，重复不可以被添加
        int checkPhoneNum = baseMapper.queryUserByPhoneNum(request.getPhoneNumber());
        if (checkPhoneNum > 0) {
            return AjaxResult.error("电话号码已存在");
        }
        sysUser.setNickName(request.getNickName());
        sysUser.setPositionName(request.getPosition());
        sysUser.setRolesId(request.getRoleId());
        sysUser.setRoleName(request.getRoleName());
        sysUser.setIdCardType(request.getIdCardType());
        sysUser.setIdCardNum(AESUtils.Encrypt(request.getIdCardNum()));
        sysUser.setDateOfBirth(simpleDateFormatymd.parse(request.getDateOfBirth()));
        sysUser.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
        sysUser.setPhonenumber(AESUtils.Encrypt(request.getPhoneNumber()));
        sysUser.setSex(request.getSex());
        sysUser.setDirectSuperior(request.getDirectSuperior());
        sysUser.setStatus(request.getStatus());
        sysUser.setDelFlag(0);
        sysUser.setCreateBy(SecurityUtils.getUsername());
        sysUser.setCreateTime(new Date());
        sysUser.setUserName(request.getUserName());
        sysUser.setDeptTree(JSON.toJSONString(request.getDeptTree()));
        int i = baseMapper.insert(sysUser);

        if (i == 0) {
            return AjaxResult.error();
        }
        //员工添加成功时对其关联的部门的员工数量+1
        try {
            deptStuffCount(request.getDeptTree());
        } catch (Exception e) {
            log.error("员工添加成功时对其关联的部门的员工数量出现异常,异常信息：{}",e.getMessage());
        }
        return AjaxResult.success();
    }

    @Async
    private void deptStuffCount(Object deptTree){
        log.info("添加员工一位员工时，同时对其关联的部门的员工数量加一（异步执行）");
        //获取添加进来的部门树
        JSONObject deptTreeObj = (JSONObject) JSON.toJSON(deptTree);
        //获取部门id集合
        JSONArray deptIdArray = deptTreeObj.getJSONArray("value");
        //部门id集合
        List<Long> deptIds = deptIdArray.toJavaList(Long.class);

        int i = sysDeptMapper.adduserAndStuffCountAdd(deptIds);
        log.info("添加员工一位员工时，同时对其关联的部门的员工数量加一（异步执行完成）");
    }
    /**
     * 根据部门树结果解析出 主公司与分公司的id 与 名称
     * 获取直属部门的id与名称
     *
     * @param deptTree
     * @param sysUser
     * @return
     */
    private SysUser dealDeptTree(Object deptTree, SysUser sysUser) {
        //从部门结构树中获取 1。最大的部门id 2所属的部门id与名称
        JSONObject deptTreeObj = (JSONObject) JSON.toJSON(deptTree);
        if (CollectionUtils.isEmpty(deptTreeObj.getJSONArray("label"))
                || CollectionUtils.isEmpty(deptTreeObj.getJSONArray("value"))) {
            return null;
        }
        //获取部门id数组
        JSONArray value = deptTreeObj.getJSONArray("value");
        //获取部门名称集合
        JSONArray label = deptTreeObj.getJSONArray("label");

        if (value.size() != label.size()) {
            return null;
        }
        //若长度小于2则说明只选择了总公司一级，细分不够不添加
        if (value.size() < 2) {
            return null;
        }
        //设置总公司
        sysUser.setCompanyId(value.get(0).toString());
        sysUser.setCompanyName(label.get(0).toString());
        //用第部门id组的下标1位去查询部门标，若能查到说明是分公司
        SysDept sysDept = sysDeptMapper.selectDeptByCpyId(value.getLong(1));
        if (sysDept != null) {
            sysUser.setFilialeId(value.getLong(1));
            sysUser.setFilialeName(label.getString(1));
        }
        sysUser.setDeptId(value.getLong(value.size() - 1));
        sysUser.setDeptName(label.getString(label.size() - 1));

        return sysUser;
    }

    /**
     * 检查是否有重复的username账号
     *
     * @param userName
     * @return
     */
    @Override
    public String madeUserName(String userName) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(SysUser::getUserName, userName);
        List<SysUser> sysUsers = baseMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(sysUsers)) {
            return userName;
        }
        List<String> userNameList = sysUsers.stream().map(item -> item.getUserName()).collect(Collectors.toList());
        int i = 1;
        boolean flag = false;
        while (!flag) {
            String ment = userName + i;
            boolean contains = userNameList.contains(ment);
            if (!contains) {
                userName = ment;
                break;
            }
            ++i;
        }
        return userName;
    }

    @Override
    public Object getRoleByDeptId(SysDept sysDept) {

        return null;
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @Override
    public List<DropDownUserList> getUserList() {
        List<DropDownUserList> result = userMapper.dropDownList();
        return result;
    }

    @Override
    public List<RoleDropDown> getRoleDropDown() {
        List<RoleDropDown> roleDropDownList = sysRoleMapper.queryDropDown();
        return roleDropDownList;
    }

    @Override
    public AjaxResult statusUser(Long userId, Integer status) {
        log.info("改变用户状态接口入参:{}", userId);
        LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysUser::getStatus, status);
        updateWrapper.eq(SysUser::getUserId, userId);
        int update = baseMapper.update(null, updateWrapper);
        if (update == 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult deleteUser(DeleteUserReqVo deleteUserReqVo) {
        log.info("员工删除接口入参:{}", JSON.toJSONString(deleteUserReqVo));
        LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysUser::getDelFlag, 1);
        updateWrapper.in(SysUser::getUserId, deleteUserReqVo.getUserIdArray());
        int update = baseMapper.update(null, updateWrapper);
        if (update == 0) {
            return AjaxResult.error();
        }
        deleteUserUpdateStuffCount(deleteUserReqVo.getUserIdArray());
        return AjaxResult.success();
    }

    /**
     * 删除员工时要修改对应的部门的员工数量 异步执行
     * @param params 员工id
     */
    @Async
    private void deleteUserUpdateStuffCount(List<Long> params){
        log.info("删除员工异步执行将部门员工数量修改 开始");
        //查询要删除的员工信息集合
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysUser::getUserId,params);
        List<SysUser> sysUserList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sysUserList)){
            return;
        }
        //创建一个容器集合用于存放部门id
        Map<Long,Long> deptIdMap = new HashMap<>();
        for (SysUser sysUser : sysUserList){
            //若存在部门树为空脏数据则不做任何处理
            if (sysUser.getDeptTree() ==null){
                continue;
            }
            JSONObject deptTreeObj = JSONObject.parseObject(sysUser.getDeptTree().toString());
            //若当前员工的部门树里存在脏数据则不做任何处理
            if (deptTreeObj.getJSONArray("value")==null){
                continue;
            }
            JSONArray deptIds = deptTreeObj.getJSONArray("value");
            //遍历当前员工信息树的部门id,记录下本次删除的员工所属部门的员工数量的减数
            for (int index = 0;index<deptIds.size();index++){
                //获取当前部门id
                Long deptId = deptIds.getLong(index);
                //若map记录中没有此部门则将此部门加入，value添加1
                if (deptIdMap.get(deptId)==null){
                    deptIdMap.put(deptId,1l);
                }else {
                    long stuffNum = deptIdMap.get(deptId).longValue();
                    deptIdMap.put(deptId,stuffNum+1);
                }
            }
        }
        for (Long deptId:deptIdMap.keySet()){
            sysDeptMapper.deleteUserSubStuffCount(deptIdMap.get(deptId),deptId);
        }
        log.info("删除员工异步执行将部门员工数量修改 结束");

    }

    @SneakyThrows
    @Override
    public AjaxResult updateUser(UpdateUserReqVo updateUserReqVo) {
        log.info("员工信息编辑接口入参：{}", JSON.toJSONString(updateUserReqVo));
        //根据部门树结构定规
        SysUser sysUser = new SysUser();
        sysUser = dealDeptTree(updateUserReqVo.getDeptTree(), sysUser);
        //若返回结果为空，则说明组织结构树结构不对
        if (sysUser==null){
            return AjaxResult.error("员工不能直属公司,请为员工划分部门");
        }
        //开始创建数据库修改对象
        LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysUser::getNickName, updateUserReqVo.getNickName());
        updateWrapper.set(SysUser::getPhonenumber, AESUtils.Encrypt(updateUserReqVo.getPhoneNumber()));
        updateWrapper.set(SysUser::getSex, updateUserReqVo.getSex());
        updateWrapper.set(SysUser::getDateOfBirth, simpleDateFormatymd.parse(updateUserReqVo.getDateOfBirth()));
        updateWrapper.set(SysUser::getCompanyId, sysUser.getCompanyId());

        updateWrapper.set(SysUser::getCompanyName, sysUser.getCompanyName());
        if (sysUser.getFilialeId() != null && StringUtils.isNotBlank(sysUser.getFilialeName())) {
            updateWrapper.set(SysUser::getFilialeId, sysUser.getFilialeId());
            updateWrapper.set(SysUser::getFilialeName, sysUser.getFilialeName());
        }
        updateWrapper.set(SysUser::getDeptId, sysUser.getDeptId());
        updateWrapper.set(SysUser::getDeptName, sysUser.getDeptName());
        updateWrapper.set(SysUser::getIdCardType, updateUserReqVo.getIdCardType());
        updateWrapper.set(SysUser::getIdCardNum, AESUtils.Encrypt(updateUserReqVo.getIdCardNum()));
        updateWrapper.set(SysUser::getDirectSuperior, updateUserReqVo.getDirectSuperior());
        updateWrapper.set(SysUser::getPositionName, updateUserReqVo.getPosition());
        updateWrapper.set(SysUser::getRolesId, updateUserReqVo.getRoleId());
        updateWrapper.set(SysUser::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SysUser::getUpdateTime, new Date());
        updateWrapper.set(SysUser::getRoleName, updateUserReqVo.getRoleName());
        updateWrapper.set(SysUser::getStatus, updateUserReqVo.getStatus());
        updateWrapper.set(SysUser::getDeptTree, JSON.toJSONString(updateUserReqVo.getDeptTree()));
        updateWrapper.eq(SysUser::getUserId, updateUserReqVo.getUserId());
        int update = baseMapper.update(null, updateWrapper);
        if (update == 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public SysUser selectUserDetailById(Long userId) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getUserId, userId);
        List<SysUser> sysUsers = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(sysUsers)) {
            SysUser sysUser = sysUsers.get(0);
            return sysUser;
        }



        return null;
    }

    /**
     * 构建返回vo
     *
     * @param sysUserList
     * @return
     */
    private List<QueryUserListRespVo> buildUserVo(List<SysUser> sysUserList) {
        List<QueryUserListRespVo> result = new ArrayList<>();
        Date nowDate = new Date();
        sysUserList.stream().forEach(sysUser -> {
            QueryUserListRespVo queryUserListRespVo = null;
            try {
                queryUserListRespVo = QueryUserListRespVo.builder()
                        .userId(sysUser.getUserId())
                        .userName(sysUser.getUserName())
                        .nickName(sysUser.getNickName())
                        .phoneNumber(AESUtils.Decrypt(sysUser.getPhonenumber()))
                        .dateOfBirth(sysUser.getDateOfBirth())
                        .age(DateUtil.betweenYear(nowDate, sysUser.getDateOfBirth(), true))
                        .cityCode(sysUser.getCityCode())
                        .cityName(sysUser.getCityName())
                        .companyId(sysUser.getCompanyId())
                        .companyName(sysUser.getCompanyName())
                        .filialeId(sysUser.getFilialeId())
                        .filialeName(sysUser.getFilialeName())
                        .deptId(sysUser.getDeptId())
                        .deptName(sysUser.getDeptName())
                        .positionName(sysUser.getPositionName())
                        .roleId(sysUser.getRolesId())
                        .roleName(sysUser.getRoleName())
                        .createTime(simpleDateFormat.format(sysUser.getCreateTime())).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            switch (sysUser.getSex()) {
                case "0":
                    queryUserListRespVo.setSex("男");
                    break;
                case "1":
                    queryUserListRespVo.setSex("女");
                    break;
                default:
                    queryUserListRespVo.setSex("未知");
            }
            queryUserListRespVo.setStatus(String.valueOf(sysUser.getStatus()));
            result.add(queryUserListRespVo);
        });
        return result;
    }


}
