package com.weilango.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddAreaReqVo {

    /**
     * 区域名称
     */
    @NotBlank(message = "区域名称 areaName不可以为空")
    private String areaName;

    /**
     * 涵盖区域
     */
    @NotNull(message = "涵盖范围 areaRange不可以为空")
    private Object areaRange;
}
