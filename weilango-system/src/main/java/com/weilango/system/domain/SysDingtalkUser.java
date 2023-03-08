package com.weilango.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉用户对象 sys_dingtalk_user
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dingtalk_user")
public class SysDingtalkUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userid;

    /** unionid */
    @Excel(name = "unionid")
    private String unionid;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String stateCode;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 部门列表 */
    @Excel(name = "部门列表")
    private String deptIdList;

    /** 角色id */
    @Excel(name = "角色id")
    private String deptOrderList;

    /** 其他信息 */
    @Excel(name = "其他信息")
    private String extension;

    /** 角色名 */
    @Excel(name = "角色名")
    private String leaderInDept;

    /** title */
    @Excel(name = "title")
    private String title;

    /** 删除标志（0代表存在 1代表删除） */
    @Excel(name = "删除标志", readConverterExp = "0=代表存在,1=代表删除")
    private String mobile;

    /** 员工工号 */
    @Excel(name = "员工工号")
    private String jobNumber;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 角色 */
    @Excel(name = "角色")
    private String roleList;

    /** 工作地点 */
    @Excel(name = "工作地点")
    private String workPlace;

    /** 员工的企业邮箱 */
    @Excel(name = "员工的企业邮箱")
    private String orgEmail;

    /** 员工的直属主管 */
    @Excel(name = "员工的直属主管")
    private String managerUserid;

}
