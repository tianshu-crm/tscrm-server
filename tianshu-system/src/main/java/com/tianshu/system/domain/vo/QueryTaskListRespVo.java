package com.tianshu.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTaskListRespVo {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 创建时间
     */
    private String createTime;

    /** 任务业务类型 01战败审批 02意向客户级别调整 */
    private String businessType;
}
