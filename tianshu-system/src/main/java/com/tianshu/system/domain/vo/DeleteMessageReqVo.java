package com.tianshu.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteMessageReqVo {

    /**
     * 用户id
     */
    @NotBlank(message = "用户 userId 不可以为空")
    private String userId;

    /**
     * 状态 0已读 1未读
     */
    private String status = "0";

    /**
     * 待删除的消息id
     */
    private List<Integer> messageId;

    /**
     * 是否全部删除 0不是 1是
     */
    @NotNull(message = "是否全部删除 isDeleteAll 不可以为空")
    private Integer isDeleteAll;

}
