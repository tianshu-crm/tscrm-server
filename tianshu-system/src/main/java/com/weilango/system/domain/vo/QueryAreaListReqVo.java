package com.weilango.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryAreaListReqVo {

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域城市名称
     */
    private String cityName;

    /**
     * 当前页
     */
    @NotNull(message = "当前页不可以为空")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @NotNull(message = "每页显示条数不可以为空")
    private Integer pageSize;
}
