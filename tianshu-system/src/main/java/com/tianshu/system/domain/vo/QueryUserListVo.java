package com.tianshu.system.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryUserListVo {

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 分公司名称
     */
    private String filialeName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 当前页
     */
    @NotNull(message = "当前页不可以为空")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @NotNull(message = "每页显示条数不可以为空")
    private Integer pageSize;
}
