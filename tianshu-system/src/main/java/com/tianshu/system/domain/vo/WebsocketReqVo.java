package com.tianshu.system.domain.vo;

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
public class WebsocketReqVo {

    @NotBlank(message = "用户id userid不可以为空")
    private String userId;

    @NotNull(message = "消息通知描述 notice不可以为空")
    private Object notice;
}
