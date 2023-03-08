package com.tianshu.crm.domain.vo;

import com.tianshu.common.annotation.Excel;
import lombok.Data;

/**
 * 试乘试驾对象 vo
 * 
 * @author hao
 * @date 2023-01-11
 */
@Data
public class CustomerTrialrunVo
{
    private static final long serialVersionUID = 1L;

    private Long trialrunId;

    /** 试驾单号 */
    @Excel(name = "试驾单号")
    private String trialrunNo;

    /** 试驾类型 */
    @Excel(name = "试驾类型")
    private String type;

    /** 试驾状态 */
    @Excel(name = "试驾状态")
    private String status;

    /** 门店编码 */
    @Excel(name = "门店编码")
    private String storeCode;

    /** 门店名称 */
    @Excel(name = "门店名称")
    private String storeName;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String name;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;


}
