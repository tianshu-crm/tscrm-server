package com.weilango.crm.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weilango.common.annotation.Excel;
import com.weilango.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索管理对象 crm_customer
 * 
 * @author hao
 * @date 2023-01-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer")
public class Customer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String name;

    /** 线索类型 */
    @Excel(name = "线索类型")
    private String type;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 微信号码 */
    @Excel(name = "微信号码")
    private String wechat;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 意向级别 */
    @Excel(name = "意向级别")
    private String level;

    /** 客户性质 */
    @Excel(name = "客户性质")
    private String nature;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 线索阶段 */
    @Excel(name = "线索阶段")
    private String stage;

    /** 线索状态 */
    @Excel(name = "线索状态  0待分配，1待跟进，2跟进中，3，已完成，4，战败")
    private String status;

    /** 客户来源 */
    @Excel(name = "客户来源")
    private String source;

    /** 导入批号 */
    @Excel(name = "导入批号")
    private String importNo;

    /** 导入批号 */
    @Excel(name = "导入类型")
    private String importType;

    /** 推荐人 */
    @Excel(name = "推荐人")
    private String referrer;

    /** 推荐人手机号 */
    @Excel(name = "推荐人手机号")
    private String referrerPhone;

    /** 推荐人微信号 */
    @Excel(name = "推荐人微信号")
    private String referrerWechat;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分配时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date allotTime;

    /** 下次沟通日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次沟通日期")
    private Date nextTime;

    /** 所属顾问 */
    @Excel(name = "所属顾问")
    private Long userId;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    //搜索关键字关键字
    @TableField(exist = false)
    private String key;

    @TableField(exist = false)
    private String lastTime;

    @TableField(exist = false)
    private String newTime;

    @TableField(exist = false)
    private Long customerId;

}
