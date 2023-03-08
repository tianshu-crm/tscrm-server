package com.tianshu.web.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据接收统一响应信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionRes {

    /**
     * 响应码
     */
    private Integer status;

    /**
     * 响应提示
     */
    private String message;

    /**
     * 相应信息载体
     */
    private Object data;
}
