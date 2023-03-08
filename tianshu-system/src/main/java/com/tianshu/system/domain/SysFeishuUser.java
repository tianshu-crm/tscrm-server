package com.tianshu.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tianshu.common.annotation.Excel;
import com.tianshu.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书用户对象 sys_feishu_user
 * 
 * @author hao
 * @date 2023-02-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_feishu_user")
public class SysFeishuUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** unionid */
    @Excel(name = "unionid")
    private String unionId;

    /** open_id */
    @Excel(name = "open_id")
    private String openId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String mobile;

    /** 性别 */
    @Excel(name = "性别")
    private Integer gender;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String departmentIds;

    /** 直属主管 */
    @Excel(name = "直属主管")
    private String leaderUserId;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 工位 */
    @Excel(name = "工位")
    private String workStation;

    /** 入职时间 */
    @Excel(name = "入职时间")
    private Long joinTime;

    /** 是否管理员 */
    @Excel(name = "是否管理员")
    private Integer isTenantManager;

    /** 工号 */
    @Excel(name = "工号")
    private String employeeNo;

    /** 角色 */
    @Excel(name = "角色")
    private String jobTitle;

}
