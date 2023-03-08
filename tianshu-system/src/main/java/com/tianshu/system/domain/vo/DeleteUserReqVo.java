package com.tianshu.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteUserReqVo {

    /**
     * userid集合
     */
    @NotNull(message = "集合 userId 用户id不可以为空")
    private List<Long> userIdArray;


}
