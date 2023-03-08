package com.weilango.crm.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索级别调整对象 crm_customer_level
 * 
 * @author hao
 * @date 2023-01-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_level")
public class CustomerLevel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 原始级别 */
    @Excel(name = "原始级别")
    private String orgLevel;

    /** 线索id */
    @Excel(name = "线索id")
    private Long customerId;

    /** 级别 */
    @Excel(name = "级别")
    private String level;

    /** 转移原因 */
    @Excel(name = "转移原因")
    private String reason;

    /** 下次跟进日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次跟进日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextTime;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
