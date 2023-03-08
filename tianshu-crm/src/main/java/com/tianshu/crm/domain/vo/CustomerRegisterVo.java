package com.tianshu.crm.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tianshu.common.annotation.Excel;
import lombok.Data;

/**
 * 来店登记对象 vo
 * 
 * @author hao
 * @date 2023-01-11
 */
@Data
public class CustomerRegisterVo
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long registerId;

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
    @Excel(name = "意向车型")
    private String intentionCarSeries;

    /** 意向车系 */
    @Excel(name = "意向车系")
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
    private String userName;

    /** 客户来源 */
    //@Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String createTime;

    private String source;

}
