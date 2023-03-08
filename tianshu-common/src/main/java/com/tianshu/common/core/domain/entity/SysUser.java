package com.tianshu.common.core.domain.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianshu.common.annotation.Excel;
import com.tianshu.common.annotation.Excels;
import lombok.Data;
import com.tianshu.common.core.domain.BaseEntity;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    @TableId(type = IdType.AUTO)
    private Long userId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Excel.Type.IMPORT)
    private Long deptId;

    @Excel(name = "部门树结构")
    private Object deptTree;

    @Excel(name = "公司id")
    private String companyId;

    @Excel(name = "公司名称")
    private String companyName;

    @Excel(name = "分公司id")
    private Long filialeId;

    @Excel(name = "分公司名称")
    private String filialeName;

    @Excel(name = "城市行政划分代码")
    private Integer cityCode;

    @Excel(name = "城市名称")
    private String cityName;

    @Excel(name = "部门名称")
    private String deptName;

    @Excel(name = "岗位名称")
    private String positionName;

    @Excel(name = "角色id")
    private Long rolesId;
    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    private String roleName;

    /**
     * 出生日期
     */
    @Excel(name = "出生日期")
    private Date dateOfBirth;

    /** 用户账号 */
    @Excel(name = "登录名称")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    private String nickName;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    @Excel(name = "证件类型")
    private String idCardType;

    @Excel(name = "证件号码")
    private String idCardNum;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /**
     * 直属上级
     */
    private String directSuperior;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private Integer status;

    /** 删除标志（0代表存在 2代表删除） */
    private Integer delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Excel.Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginDate;

    /** 部门对象 */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Excel.Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Excel.Type.EXPORT)
    })
    @TableField(exist = false)
    private SysDept dept;

    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRole> roles;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    /** 岗位组 */
    @TableField(exist = false)
    private Long[] postIds;

    /** 角色ID */
    @TableField(exist = false)
    private Long roleId;

    //开始时间
    @TableField(exist = false)
    private String startDate;

    //结束时间
    @TableField(exist = false)
    private String endDate;

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public SysDept getDept()
    {
        return dept;
    }
}
