package com.tianshu.system.domain.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tianshu.common.annotation.Excel;
import com.tianshu.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * 系统消息对象 sys_message
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
@Data
@TableName("sys_message")
public class SysMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据序列 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 消息类型 1任务消息 2系统消息 3审批消息 */
    @Excel(name = "消息类型 1任务消息 2系统消息 3审批消息")
    private String type;

    /** 审批状态 1已审批 2未审批 3已驳回 */
    @Excel(name = "审批状态 1已审批 2未审批 3已驳回")
    private String approval;

    /** 消息被创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "消息被创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 消息所属人id */
    @Excel(name = "消息所属人id")
    private String userId;

    /** 消息主题 */
    @Excel(name = "消息主题")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String detail;

    /** 是否已读 0已读 1未读 */
    @Excel(name = "是否已读 0已读 1未读")
    private String status;

    /** 消息业务类型 */
    @Excel(name = "消息业务类型")
    private String businessType;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
