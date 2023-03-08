package com.weilango.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.weilango.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryMessageRespVo {


    /** 数据序列 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 消息类型 1任务消息 2系统消息 3审批消息 */
    @Excel(name = "消息类型 1任务消息 2系统消息 3审批消息")
    private String type;

    /** 审批状态 1已审批 2未审批 3已驳回 */
    @Excel(name = "审批状态 1已审批 2未审批 3已驳回 4不需要审批")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String approval;

    /** 消息被创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Excel(name = "消息被创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String sendTime;

    /** 消息所属人id */
    @Excel(name = "消息所属人id")
    private String userId;

    /** 消息主题 */
    @Excel(name = "消息主题")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String detail;

    /** 是否已读 0已读 1未读 */
    @Excel(name = "是否已读 0已读 1未读")
    private String status;


}
