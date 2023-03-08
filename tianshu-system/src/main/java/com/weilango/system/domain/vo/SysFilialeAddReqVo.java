package com.weilango.system.domain.vo;

import com.weilango.common.annotation.Excel;
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
public class SysFilialeAddReqVo {

    @NotNull(message = "城市行政划分代码 cityCode不可以为空")
    private Integer cityCode;

    /** 所在城市 */
    @NotBlank(message = "所在城市 city 不可以为空")
    private String cityName;

    @NotNull(message = "公司id companyId 不可以为空")
    private Long companyId;

    /** 公司名称 */
    @NotBlank(message = "公司名称 companyName 不可以为空")
    private String companyName;
    @NotBlank(message = "分公司名称 filialeName 不可以为空")
    private String filialeName;

    /** 公司注册地址 */
    @NotBlank(message = "分公司地址 filialeAddress 不可以为空")
    private String filialeAddress;

    /** 社会信用代码 */
    @NotBlank(message = "分公司社会信用代码 identifier不可以为空")
    private String filialeIdentifier;

    /** 法人代表 */
    @NotBlank(message = "负责人 generalManager 不可以为空")
    private String generalManager;

    /**
     * 公司联系电话
     */
    @NotBlank(message = "分公司联系电话 filialePhone 不可以为空")
    private String filialePhone;


    /** 开户行 */
    @NotBlank(message = "分公司开户行 filialeOpenBank 不可以为空")
    private String filialeOpenBank;


    private Object cityCodeList;

    /** 公户号 */
    @NotBlank(message = "分公司公户号 filialeAccountNum 不可以为空")
    private String filialeAccountNum;
}
