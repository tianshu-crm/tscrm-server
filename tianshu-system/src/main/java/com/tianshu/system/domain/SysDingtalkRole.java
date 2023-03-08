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
 * 钉钉角色对象 sys_dingtalk_role
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dingtalk_role")
public class SysDingtalkRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 角色组id */
    @Excel(name = "角色组id")
    private Long groupId;

    /** 角色组名称 */
    @Excel(name = "角色组名称")
    private String groupName;

    /** 角色id */
    @Excel(name = "角色id")
    private Long roleId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String name;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
