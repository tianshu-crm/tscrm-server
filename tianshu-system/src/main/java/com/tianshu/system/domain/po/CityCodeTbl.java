package com.tianshu.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("crm_city_tbl")
public class CityCodeTbl {

    @TableId(type = IdType.AUTO)
    private Long cityId;

    private Integer regionCode;

    private String regionName;

    private Integer regionLevel;

    private Integer parentRegionCode;
}
