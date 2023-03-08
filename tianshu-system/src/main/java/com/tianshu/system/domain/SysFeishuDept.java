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
 * 飞书部门对象 sys_feishu_dept
 * 
 * @author hao
 * @date 2023-02-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_feishu_dept")
public class SysFeishuDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String name;

    /** 部门id */
    @Excel(name = "部门id")
    private String departmentId;

    /** 父部门ID */
    @Excel(name = "父部门ID")
    private String parentDepartmentId;

    /** 部门的open_id */
    @Excel(name = "部门的open_id")
    private String openDepartmentId;

    /** 部门主管用户ID */
    @Excel(name = "部门主管用户ID")
    private String leaderUserId;

    /** 部门下用户的个数 */
    @Excel(name = "部门下用户的个数")
    private Integer memberCount;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
