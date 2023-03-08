package com.tianshu.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysCompanyUpdateReqVo {

    /** 公司ID */
    @NotNull(message = "公司ID companyId 不可以为空")
    private Long companyId;

    @NotNull(message = "城市行政划分代码不可以为空 cityCode 不可以为空")
    private Integer cityCode;

    /** 所在城市 */
    @NotBlank(message = "所在城市 city 不可以为空")
    private String cityName;

    @NotBlank(message = "公司注册时间 companyRegTime 不可以为空")
    private String companyRegTime;
    /** 公司名称 */
    @NotBlank(message = "公司名称 companyName 不可以为空")
    private String companyName;

    /** 公司注册地址 */
    @NotBlank(message = "公司注册地址 companyRegAddress 不可以为空")
    private String companyRegAddress;

    /** 社会信用代码 */
    @NotBlank(message = "社会信用代码 identifier不可以为空")
    private String identifier;

    /** 法人代表 */
    @NotBlank(message = "法人代表 legalPersonName 不可以为空")
    private String legalPersonName;

    /**
     * 公司联系电话
     */
    @NotBlank(message = "公司联系电话 companyPhone 不可以为空")
    private String companyPhone;


    /** 开户行 */
    @NotBlank(message = "开户行 accountOpenBank 不可以为空")
    private String accountOpenBank;

    private Object cityCodeList;

    /** 公户号 */
    @NotBlank(message = "公户号 accountNum 不可以为空")
    private String accountNum;

}
