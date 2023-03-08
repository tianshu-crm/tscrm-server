package com.tianshu.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserListRespVo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户名称
     */
    private String nickName;


    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 生日
     */
    private Date dateOfBirth;

    /**
     * 年龄
     */
    private Long age;

    /**
     * 城市行政编码
     */
    private Integer cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 分公司id
     */
    private Long filialeId;

    /**
     * 分公司名称
     */
    private String filialeName;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    private String createTime;
}
