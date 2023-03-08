package com.weilango.crm.domain.vo;

import com.weilango.common.annotation.Excel;
import lombok.Data;

/**
 * 战败池
 * 
 * @author hao
 * @date 2023-01-09
 */
@Data
public class CustomerFailureVo
{
    private static final long serialVersionUID = 1L;

    private String key;
    private String customerId;
    private String failureId;
    @Excel(name = "客户姓名")
    private String name;
    @Excel(name = "手机号码")
    private String phonenumber;
    @Excel(name = "微信号")
    private String wechat;
    @Excel(name = "业务类型")
    private String businessType;
    @Excel(name = "意向车型")
    private String intentionCarType;
    @Excel(name = "意向车系")
    private String intentionCarSeries;
    @Excel(name = "所属顾问")
    private String userName;
    @Excel(name = "战败时间")
    private String failureTime;
    @Excel(name = "战败原因")
    private String reason;
    @Excel(name = "战败说明")
    private String remark;
    @Excel(name = "所属顾问Id")
    private String userId;


}
