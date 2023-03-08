package com.tianshu.crm.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tianshu.common.annotation.Excel;
import com.tianshu.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索轨迹对象 crm_customer_track
 *
 * @author hao
 * @date 2023-01-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("crm_customer_track")
public class CustomerTrack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 线索id */
    @Excel(name = "线索id")
    private Long customerId;

    /** 跟进方式 */
    @Excel(name = "跟进方式")
    private String type;

    /** 通话时长（单位分钟） */
    @Excel(name = "通话时长", readConverterExp = "单=位分钟")
    private Long callDuration;

    /** 跟进内容 */
    @Excel(name = "跟进内容")
    private String content;

    /** 下次跟进日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次跟进日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextTime;

    /** 下次跟进内容 */
    @Excel(name = "下次跟进内容")
    private String nextContent;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private String trackDate;

}
