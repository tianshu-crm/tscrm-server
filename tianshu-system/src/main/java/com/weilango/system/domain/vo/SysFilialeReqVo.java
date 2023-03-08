package com.weilango.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysFilialeReqVo {


    /**
     * 分公司名称
     */
    private String filialeName;

    /**
     * 当前页
     */
    @NotNull(message = "当前页 pageNum 不可以为空")
    private Integer pageNum;

    /**
     * 每页条数
     */
    @NotNull(message = "当前页 pageSize 不可以为空")
    private Integer pageSize;

}
