package com.tianshu.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.system.domain.vo.SysWechattalkDept;

/**
 * 用户与角色关联表 数据层
 *
 * @author ruoyi
 */
public interface SysWechattalkMapper extends BaseMapper<SysWechattalkDept>
{

    //将微信部门列表插入到数据库中
    public int insertDeptList(List<SysWechattalkDept> result);


}