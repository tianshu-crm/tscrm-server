package com.tianshu.crm.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tianshu.common.annotation.Excel;
import com.tianshu.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索详情对象 crm_customer_detail
 * 
 * @author hao
 * @date 2023-01-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_detail")
public class CustomerDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 线索id */
    @Excel(name = "线索id")
    private Long customerId;

    /** 证件类型 */
    @Excel(name = "证件类型")
    private String certificateType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String certificateNo;

    /** 意向车系 */
    @Excel(name = "意向车系")
    private String intentionCarSeries;

    /** 意向车型 */
    @Excel(name = "意向车型")
    private String intentionCarType;

    /** 购买车型 */
    @Excel(name = "购车类型")
    private String purchaseCarType;

    /** 购车预算 */
    @Excel(name = "购车预算")
    private String purchaseCarBudget;

    /** 购车时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购车时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseCarTime;

    /** 是否网约车从业者 */
    @Excel(name = "是否网约车从业者")
    private String isOnlineCarUser;

    /** 是否有人证 */
    @Excel(name = "是否有人证")
    private String isCertificate;

    /** 是否留档 */
    private String isFile;

    /** 意向说明 */
    @Excel(name = "意向说明")
    private String intentionDesc;

    /** 竞争公司 */
    @Excel(name = "竞争公司")
    private String competeCompany;

    /** 竞对备注 */
    @Excel(name = "竞对备注")
    private String competeDesc;

    /** 竞争对手 */
    @Excel(name = "竞争对手")
    private String competeCar;

    /** 用户描述 */
    @Excel(name = "用户描述")
    @TableField("`desc`")
    private String desc;



    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

}
