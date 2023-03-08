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
 * 钉钉部门对象 sys_dingtalk_dept
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dingtalk_dept")
public class SysDingtalkDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String name;

    /** 父部门ID */
    @Excel(name = "父部门ID")
    private Long parentId;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
