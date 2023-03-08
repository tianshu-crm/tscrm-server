package com.weilango.system.domain.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * 【请填写功能名称】对象 sys_task
 * 
 * @author hao
 * @date 2023-01-13
 */
@Data
@TableName("sys_task")
public class SysTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id
 */
    @TableId(type = IdType.AUTO)
    private Long id;

    @Excel(name = "任务处理人id")
    private Long processorId;

    /** 任务标题 */
    @Excel(name = "任务标题")
    private String title;

    /** 任务类型：1待处理任务 2已处理任务 3已发起任务  */
    @Excel(name = "任务类型：1待处理任务 2已处理任务 3已发起任务 ")
    private String type;

    /** 任务业务类型 01战败审批 02意向客户级别调整 */
    @Excel(name = "任务业务类型 1战败审批 2意向客户级别调整")
    private String businessType;

    /** 业务id*/
    private Long businessId;

    /** 发起人id */
    @Excel(name = "发起人id")
    private Long sponsorUserId;

    /** 发起人名称 */
    @Excel(name = "发起人名称")
    private String sponsorUserName;

    /** 所属单位 */
    @Excel(name = "所属单位")
    private String affiliatedUnit;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sponsorTime;

    /** 审批留言 */
    @Excel(name = "审批留言")
    private String approvalLeaveMsg;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalTime;

    /** 01已审批 02 未审批 03 已驳回 04 不需要审批 */
    @Excel(name = "1已审批 2 未审批 3 已驳回 4 不需要审批")
    private String approvalStatus;

    /** 任务详情 */
    @Excel(name = "任务详情")
    private String detail;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
