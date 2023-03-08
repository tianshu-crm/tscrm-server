package com.weilango.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTaskDetailResVo {

    /**
     * 任务id
     */
    private String id;

    /**
     * 任务业务类型
     */
    private String taskBusinessType;

    /**
     * 发送人姓名
     */
    private String sponsorUserName;


    /**
     * 发起人所属单位
     */
    private String affiliatedUnit;

    /**
     * 发起时间
     */
    private String sponsorTime;

    /**
     * 任务详情
     */
    private Object taskDetail;

    private String title;

    private String approvalStatus;
}
