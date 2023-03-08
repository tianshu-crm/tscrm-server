package com.weilango.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckAreaNameIsExist {

    @NotBlank(message = "区域名称 areaName 当前区域不可以为空")
    private String areaName;

    /**
     * 区域id在编辑的时候传递
     */
    private String areaId;

}
