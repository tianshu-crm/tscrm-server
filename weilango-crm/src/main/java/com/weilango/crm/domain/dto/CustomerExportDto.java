package com.weilango.crm.domain.vo;

import com.weilango.common.annotation.Excel;
import lombok.Data;

/**
 * 线索管理对象 crm_customer
 *
 * @author hao
 * @date 2023-01-09
 */
@Data
public class CustomerExportDto
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "客户姓名")
    private String name;
    @Excel(name = "手机号码")
    private String phonenumber;
    @Excel(name = "微信号")
    private String wechat;
    @Excel(name = "性别",dictType = "sys_user_sex")
    private String sex;
    @Excel(name = "年龄")
    private String age;
    @Excel(name = "意向级别",dictType = "crm_customer_level")
    private String level;
    @Excel(name = "客户性质",dictType = "crm_customer_nature")
    private String nature;
    @Excel(name = "业务类型",dictType = "crm_customer_businessType")
    private String businessType;
    @Excel(name = "线索阶段",dictType = "crm_customer_stage")
    private String stage;
    //@Excel(name = "线索状态")
    private String status;
    //@Excel(name = "客户来源")
    private String source;
    private String importNO;
    //@Excel(name = "创建人")
    private String createBy;
    @Excel(name = "推荐人")
    private String referrer;
    @Excel(name = "推荐人手机号")
    private String referrerPhone;
    @Excel(name = "推荐人微信号")
    private String referrerWechat;
    //@Excel(name = "创建日期")
    private String createTime;
    //@Excel(name = "最后修改日期")
    private String updateTime;

}
