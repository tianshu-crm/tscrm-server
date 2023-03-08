package com.tianshu.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAreaRangeLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer cityCode;

    private String cityName;

    private String areaId;

    private String areaName;
}
