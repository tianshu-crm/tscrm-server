package com.tianshu.crm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 线索管理对象 crm_customer
 * 
 * @author hao
 * @date 2023-01-09
 */
@Data
public class CustomerRegisterAddDto
{
    private static final long serialVersionUID = 1L;
    /** ID */
    private Long id;

    /** 客户姓名 */
    private String name;

    /** 手机号码 */
    private String phonenumber;

    /** 微信号码 */
    private String wechat;

    /** 来店次数 */
    private Long times;

    /** 来访类别 */
    private String type;

    /** 来访目的 */
    private String purpose;

    /** 意向车型 */
    private String intentionCarSeries;

    /** 意向车系 */
    private String intentionCarType;

    /** 推荐人 */
    private String referrer;

    /** 推荐人手机号 */
    private String referrerPhone;

    /** 推荐人微信号 */
    private String referrerWechat;

    /** 接待顾问 */
    private Long userId;

    /** 接待时长 */
    private Long duration;

    /** 来店人数 */
    private Long number;

    /** 来店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 离店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 是否产品介绍1是，0否 */
    private String isIntroduce;

    /** 是否经理参与1是，0否 */
    private String isManagerParticipation;

    /** 客户来源 */
    private String source;

    /** 驾车车型 */
    private String carType;

    /** 车牌号 */
    private String carNo;

}
