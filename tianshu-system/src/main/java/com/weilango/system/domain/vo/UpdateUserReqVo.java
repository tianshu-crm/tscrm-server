package com.weilango.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateUserReqVo {

    /**
     * 用户id
     */
    @NotNull(message = "用户id userid 不不可以为空")
    private Long userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称 nickName不可以为空")
    private String nickName;

    /**
     * 电话号
     */
    @NotBlank(message = "电话号 phoneNumber不可以为空")
    private String phoneNumber;

    /**
     * 性别 0男 1女 2未知
     */
    @NotBlank(message = "性别 sex 不可以为空")
    private String sex;

    /**
     * 出生日期
     */
    @NotBlank(message = "生日 dateOfBirth 不可以为空")
    private String dateOfBirth;

    @NotNull(message = "部门组织树 deptTree 不可以为空")
    private Object deptTree;

    @NotBlank(message = "证件类型 idCardType 不可以为空")
    private String idCardType;

    @NotBlank(message = "证件号码 idCardNum 不可以为空")
    private String idCardNum;

    /**
     * 直属上级
     */
    @NotBlank(message = "直属上级 directSuperior 不可以为空")
    private String directSuperior;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称 position 不可以为空")
    private String position;

    /**
     * 角色id
     */
    @NotNull(message = "角色id roleId 不可以为空")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称 roleName 不可以为空")
    private String roleName;

    /**
     * 帐号状态（0正常 1停用）
     */
    @NotNull(message = "帐号状态 status 不可以为空")
    private String status;
}
