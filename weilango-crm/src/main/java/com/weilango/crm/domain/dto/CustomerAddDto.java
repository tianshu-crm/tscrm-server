package com.weilango.crm.domain.dto;

import com.weilango.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 线索管理对象 crm_customer
 * 
 * @author hao
 * @date 2023-01-09
 */
@Data
public class CustomerAddDto extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客户姓名 */
    private String name;

    /** 手机号码 */
    private String phonenumber;

    /** 微信号码 */
    private String wechat;

    /** 性别 */
    private String sex;

    /** 年龄 */
    private Long age;

    /** 意向级别 */
    private String level;

    /** 用户描述 */
    private String desc;

    /** 客户性质 */
    private String nature;

    /** 业务类型 */
    private String businessType;

    /** 线索阶段 */
    private String stage;

    /** 线索状态 */
    private String status;

    /** 意向车系 */
    private String intentionCarSeries;

    /** 意向车型 */
    private String intentionCarType;

    /** 购车类型 */
    private String purchaseCarType;

    /** 购车预算 */
    private String purchaseCarBudget;

    /** 购车时间 */
    private Date purchaseCarTime;

    /** 是否网约车从业者 */
    private String isOnlineCarUser;

    /** 意向说明 */
    private String intentionDesc;

    /** 推荐人 */
    private String referrer;

    /** 推荐人手机号 */
    private String referrerPhone;

    /** 推荐人微信号 */
    private String referrerWechat;

    /** 线索id */
    private Long customerId;

    /** 备注 */
    private String competeDesc;

    /** 竞争公司 */
    private String competeCompany;

    /** 竞争品牌车型 */
    private String competeCar;

    /** 证件类型 */
    private String certificateType;

    /** 证件号码 */
    private String certificateNo;

    /** 是否有人证 */
    private String isCertificate;

    /** 是否留档 */
    private String isFile;

    /** 客户来源 */
    private String source;


    /** 跟进方式 */
    private String type;

    /** 通话时长（单位分钟） */
    private Long callDuration;

    /** 跟进内容 */
    private String content;

    /** 下次跟进日期 */
    private Date nextTime;

    /** 下次跟进内容 */
    private String nextContent;

}
