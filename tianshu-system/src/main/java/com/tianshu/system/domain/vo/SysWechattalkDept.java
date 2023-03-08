package com.tianshu.system.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysWechattalkDept {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 部门id */
    private Long deptId;

    /** 部门名称 */
    private String name;

    /** 父部门ID */
    private Long parentId;
    /** 删除标志（0代表存在 1代表删除） */
    private Long order;

    private String departmentLeader;

}
