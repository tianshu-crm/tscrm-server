package com.weilango.crm.domain;

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
 * 线索战败对象 crm_customer_failure
 * 
 * @author hao
 * @date 2023-01-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_failure")
public class CustomerFailure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 原始顾问id */
    @Excel(name = "原始顾问id")
    private Long orgUserId;

    /** 转移原因 */
    @Excel(name = "转移原因")
    private String reason;

    /** 线索id */
    @Excel(name = "线索id")
    private Long customerId;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String auditStatus;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 删除标志（0代表存在 1代表删除） */
    private String activateReason;

}
