package com.weilango.system.domain.vo;

import com.weilango.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryMessageReqVo{

    /**
     * 用户id
     */
    @NotBlank(message = "userId 用户id不可以为空")
    private String userId;

    /**
     * 查询类别 0已读消息 1未读消息 2全部
     */
    private String status = "2";

    /**
     * 消息类别 1任务消息 2系统消息 3审批消息 4进度提醒
     */
    private String messageType;

    /**
     * 当前页
     */
    @NotNull(message = "pageNum 当前页数不可以为空")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @NotNull(message = "pageSize 每页的条数不可以为空")
    private Integer pageSize;
}
