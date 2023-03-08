package com.weilango.crm.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 线索转移对象 crm_customer_transfer
 * 
 * @author hao
 * @date 2023-01-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_transfer")
public class CustomerTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 原始顾问id */
    @Excel(name = "原始顾问id")
    private Long orgUserId;

    /** 所属顾问 */
    @Excel(name = "所属顾问")
    private Long userId;

    /** 转移原因 */
    @Excel(name = "转移原因")
    private String reason;

    /** 线索id */
    @Excel(name = "线索id")
    private Long customerId;

    /** 下次跟进日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次跟进日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextTime;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    @TableField(exist = false)
    private String customerIds;

}
