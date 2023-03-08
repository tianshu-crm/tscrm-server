package com.tianshu.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskReqVo {

    /**
     * 处理人的用户id
     */
    @NotBlank(message = "操作失败")
    private Long processorId;

    /**
     * 任务标题
     */
    @NotBlank(message = "操作失败")
    private String title;

    /**
     * 任务业务类型 01战败审批 02意向客户级别调整
     */
    @NotBlank(message = "操作失败")
    private String businessType;

    /**
     * 任务发起人id
     */
    @NotNull(message = "操作失败")
    private Long sponsorUserId;

    /**
     * 任务发起人姓名
     */
    @NotBlank(message = "操作失败")
    private String sponsorUserName;

    /**
     * 所属单位
     */
    @NotBlank(message = "操作失败")
    private String affiliatedUnit;

    /**
     * 发起时间
     */
    @NotNull(message = "操作失败")
    private Date sponsorTime;

    /**
     * 任务详情
     */
    @NotNull(message = "操作失败")
    private Object detail;

    /**
     * 01已审批 02 未审批 03 已驳回 04 不需要审批
     */
    @NotBlank(message = "操作失败")
    private String approvalStatus;
}
