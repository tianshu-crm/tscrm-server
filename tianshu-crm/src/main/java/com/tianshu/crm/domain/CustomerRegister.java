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
 * 来店登记对象 crm_customer_register
 * 
 * @author hao
 * @date 2023-01-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_register")
public class CustomerRegister extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String name;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 微信号码 */
    @Excel(name = "微信号码")
    private String wechat;

    /** 来店次数 */
    @Excel(name = "来店次数")
    private Long times;

    /** 来访类别 */
    @Excel(name = "来访类别")
    private String type;

    /** 来访目的 */
    @Excel(name = "来访目的")
    private String purpose;

    /** 意向车型 */
    @Excel(name = "意向车系")
    private String intentionCarSeries;

    /** 意向车系 */
    @Excel(name = "意向车型")
    private String intentionCarType;

    /** 推荐人 */
    @Excel(name = "推荐人")
    private String referrer;

    /** 推荐人手机号 */
    @Excel(name = "推荐人手机号")
    private String referrerPhone;

    /** 推荐人微信号 */
    @Excel(name = "推荐人微信号")
    private String referrerWechat;

    /** 接待顾问 */
    @Excel(name = "接待顾问")
    private Long userId;

    /** 接待时长 */
    @Excel(name = "接待时长")
    private Long duration;

    /** 来店人数 */
    @Excel(name = "来店人数")
    private Long number;

    /** 来店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "来店时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 离店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离店时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 是否产品介绍1是，0否 */
    @Excel(name = "是否产品介绍1是，0否")
    private String isIntroduce;

    /** 是否经理参与1是，0否 */
    @Excel(name = "是否经理参与1是，0否")
    private String isManagerParticipation;

    /** 客户来源 */
    @Excel(name = "客户来源")
    private String source;

    /** 关联线索 */
    @Excel(name = "关联线索")
    private Long customerId;

    /** 驾车车型 */
    @Excel(name = "驾车车型")
    private String carType;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carNo;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    //搜索关键字关键字
    @TableField(exist = false)
    private String key;

    // 关联线索1是，0否
    @TableField(exist = false)
    private String isCustomer;

    // 关联试驾1是，0否
    @TableField(exist = false)
    private String isTrialrun;

}
