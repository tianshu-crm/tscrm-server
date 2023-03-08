package com.tianshu.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QueryTaskListReqVo {

    /**
     * 处理任务的人的id
     */
    @NotNull(message = "处理人的用户id userId 不可以为空")
    private Long userId;

    /**
     * 任务类型 1待处理任务 2已处理任务 3已发起任务
     */
    @NotBlank(message = "任务类型 taskType 不可以为空")
    private String taskType;

    /**
     * 当前页
     */
    @NotNull(message = "当前页 pageNum 不可以为空")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @NotNull(message = "每页显示条数 pageSize 不可以为空")
    private Integer pageSize;

}
