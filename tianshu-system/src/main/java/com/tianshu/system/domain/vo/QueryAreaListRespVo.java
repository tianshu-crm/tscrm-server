package com.tianshu.system.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryAreaListRespVo {

    /** 区域数据id */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 区域id */
    private String areaId;

    /** 区域名称 */
    private String areaName;

    /** 涵盖区域 */
    private Object areaRange;

    /** 状态0存在 1删除 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String delFlag;

    /** 区域下员工的数量 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long staffCount;
}
