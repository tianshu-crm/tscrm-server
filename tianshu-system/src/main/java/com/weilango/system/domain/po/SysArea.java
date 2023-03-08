package com.weilango.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * 【请填写功能名称】对象 sys_area
 * 
 * @author hao
 * @date 2023-01-10
 */
@Data
@TableName("sys_area")
public class SysArea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 区域数据id */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String areaName;

    /** 涵盖区域 */
    @Excel(name = "涵盖区域")
    private Object areaRange;

    /** 状态0存在 1删除 */
    private String delFlag;

    @TableField(exist = false)
    private String areaId;

}
