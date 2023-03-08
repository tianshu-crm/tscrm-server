package com.tianshu.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryAreaNameListRespVo {

    /**
     * 区域id
     */
    private String dictValue;

    /**
     * 区域名称
     */
    private String dictLabel;

}
