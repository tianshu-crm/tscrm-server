package com.weilango.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 分公司管理对象 sys_filiale
 *
 * @author hao
 * @date 2023-01-10
 */
@Data
@TableName("sys_filiale")
public class SysFiliale extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据序列ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 城市划分代码
     */
    @Excel(name = "城市划分代码")
    private Integer cityCode;

    /**
     * 所在城市
     */
    @Excel(name = "所在城市")
    private String cityName;

    /**
     * 分公司id
     */
    @Excel(name = "分公司id")
    private Long filialeId;

    /**
     * 公司id
     */
    @Excel(name = "公司id")
    private Long companyId;



    /**
     * 公司名称
     */
    @Excel(name = "公司名称")
    private String companyName;

    /**
     * 分公司名称
     */
    @Excel(name = "分公司名称")
    private String filialeName;

    /**
     * 分公司地址
     */
    @Excel(name = "分公司地址")
    private String filialeAddress;

    /**
     * 负责人（总经理）
     */
    @Excel(name = "负责人", readConverterExp = "总=经理")
    private String generalManager;

    /**
     * 分公司电话
     */
    @Excel(name = "分公司电话")
    private String filialePhone;

    /**
     * 分公司注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分公司注册时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date filialeRegTime;

    /**
     * 分公司人数
     */
    @Excel(name = "分公司人数")
    private String filialeStaffNum;

    /**
     * 分公司社会信用代码
     */
    @Excel(name = "分公司社会信用代码")
    private String filialeIdentifier;

    /**
     * 分公司开户行
     */
    @Excel(name = "分公司开户行")
    private String filialeOpenBank;

    /**
     * 分公司公户名称
     */
    @Excel(name = "分公司公户名称")
    private String filialeAccountName;

    /**
     * 分公司开户号
     */
    @Excel(name = "分公司开户号")
    private String filialeAccountNum;

    @Excel(name = "省市Code")
    private Object cityCodeList;


    /**
     * 0有效 1删除
     */
    private String delFlag;

}

