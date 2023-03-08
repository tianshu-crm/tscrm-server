package com.weilango.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddUserReqVo {


    @NotBlank(message = "用户账号 userName 不可以为空")
    private String userName;
    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称 nickName不可以为空")
    private String nickName;

    /**
     * 账号密码
     */
    @NotBlank(message = "用户密码 password不可以为空")
    private String password;

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
     * 部门结构树
     */
    @NotNull(message = "部门结构树 deptTree 不可以为空")
    private Object deptTree;

    /**
     * 出生日期
     */
    @NotNull(message = "生日 dateOfBirth 不可以为空")
    @JsonFormat(pattern = "yyyMMdd" ,timezone = "GMT+8")
    private String dateOfBirth;

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
    private Integer status;

}
