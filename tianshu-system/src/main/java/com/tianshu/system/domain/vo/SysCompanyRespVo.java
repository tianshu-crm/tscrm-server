package com.tianshu.system.domain.vo;

import com.tianshu.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysCompanyRespVo {

    @Excel(name = "城市行政划分代码")
    private Integer cityCode;

    @Excel(name = "所在城市")
    private String cityName;

    /** 公司ID */
    @Excel(name = "公司ID")
    private Long companyId;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 公司注册地址 */
    @Excel(name = "公司注册地址")
    private String companyRegAddress;

    /** 公司通信地址 */
    @Excel(name = "公司通信地址")
    private String companyComAddress;

    /** 公司注册时间 */
    @Excel(name = "公司注册时间")
    private  String companyRegTime;

    /** 公司电话 */
    @Excel(name = "公司电话")
    private String companyPhone;

    /** 注册资本 */
    @Excel(name = "注册资本")
    private BigDecimal regCapital;

    /** 社会信用代码 */
    @Excel(name = "社会信用代码")
    private String identifier;

    /** 公司人数 */
    @Excel(name = "公司人数")
    private String staffNum;

    /** 法人代表 */
    @Excel(name = "法人代表")
    private String legalPersonName;

    /** 法人身份证号 */
    @Excel(name = "法人身份证号")
    private String legalPersonIdcard;

    /** 法人电话 */
    @Excel(name = "法人电话")
    private String legalPersonPhone;

    /** 是够存在分公司（0有，1没有） */
    @Excel(name = "是够存在分公司", readConverterExp = "0=有，1没有")
    private String existBranchCompany;

    /** 分公司数量 */
    @Excel(name = "分公司数量")
    private Long branchCompanyNum;

    /** 公户名称 */
    @Excel(name = "公户名称")
    private String accountName;

    /** 开户行 */
    @Excel(name = "开户行")
    private String accountOpenBank;

    /** 公户号 */
    @Excel(name = "公户号")
    private String accountNum;

    /** 账户留存电话 */
    @Excel(name = "账户留存电话")
    private String accountPhone;

    /**
     * 数据被创建时间
     */
    private String dataCreateTime;
}
