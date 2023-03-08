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
 * 试乘试驾对象 crm_customer_trialrun
 * 
 * @author hao
 * @date 2023-01-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_trialrun")
public class CustomerTrialrun extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String name;

    /** 试驾类型 */
    @Excel(name = "试驾类型")
    private String type;

    /** 试驾状态 */
    @Excel(name = "试驾状态")
    private String status;

    /** 门店名称 */
    @Excel(name = "门店名称")
    private String storeName;

    /** 门店编码 */
    @Excel(name = "门店编码")
    private String storeCode;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 客户来源 */
    @Excel(name = "客户来源")
    private String source;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trialrunTime;

    /** 试驾人姓名 */
    @Excel(name = "试驾人姓名")
    private String trialrunName;

    /** 试驾人手机号 */
    @Excel(name = "试驾人手机号")
    private String trialrunPhone;

    /** 试驾车型 */
    @Excel(name = "试驾车型")
    private String trialrunCarType;

    /** 试驾车系 */
    @Excel(name = "试驾车系")
    private String trialrunCarSeries;

    /** 关联线索 */
    @Excel(name = "关联线索")
    private Long customerId;

    /** 试驾单号 */
    @Excel(name = "试驾单号")
    private String trialrunNo;

    /** 所属顾问 */
    @Excel(name = "所属顾问")
    private Long userId;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    //搜索关键字关键字
    @TableField(exist = false)
    private String key;

}
