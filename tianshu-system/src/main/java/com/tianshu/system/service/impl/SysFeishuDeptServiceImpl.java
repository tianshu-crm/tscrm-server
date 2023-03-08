package com.tianshu.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.system.mapper.SysFeishuDeptMapper;
import com.tianshu.system.domain.SysFeishuDept;
import com.tianshu.system.service.ISysFeishuDeptService;

/**
 * 飞书部门Service业务层处理
 * 
 * @author hao
 * @date 2023-02-15
 */
@Service
public class SysFeishuDeptServiceImpl extends ServiceImpl<SysFeishuDeptMapper, SysFeishuDept> implements ISysFeishuDeptService
{
    @Autowired
    private SysFeishuDeptMapper sysFeishuDeptMapper;

    /**
     * 查询飞书部门
     * 
     * @param id 飞书部门主键
     * @return 飞书部门
     */
    @Override
    public SysFeishuDept selectSysFeishuDeptById(Long id)
    {
        return sysFeishuDeptMapper.selectSysFeishuDeptById(id);
    }

    /**
     * 查询飞书部门列表
     * 
     * @param sysFeishuDept 飞书部门
     * @return 飞书部门
     */
    @Override
    public List<SysFeishuDept> selectSysFeishuDeptList(SysFeishuDept sysFeishuDept)
    {
        return sysFeishuDeptMapper.selectSysFeishuDeptList(sysFeishuDept);
    }

    /**
     * 新增飞书部门
     * 
     * @param sysFeishuDept 飞书部门
     * @return 结果
     */
    @Override
    public int insertSysFeishuDept(SysFeishuDept sysFeishuDept)
    {
        sysFeishuDept.setCreateTime(DateUtils.getNowDate());
        return sysFeishuDeptMapper.insertSysFeishuDept(sysFeishuDept);
    }

    /**
     * 修改飞书部门
     * 
     * @param sysFeishuDept 飞书部门
     * @return 结果
     */
    @Override
    public int updateSysFeishuDept(SysFeishuDept sysFeishuDept)
    {
        sysFeishuDept.setUpdateTime(DateUtils.getNowDate());
        return sysFeishuDeptMapper.updateSysFeishuDept(sysFeishuDept);
    }

    /**
     * 批量删除飞书部门
     * 
     * @param ids 需要删除的飞书部门主键
     * @return 结果
     */
    @Override
    public int deleteSysFeishuDeptByIds(Long[] ids)
    {
        return sysFeishuDeptMapper.deleteSysFeishuDeptByIds(ids);
    }

    /**
     * 删除飞书部门信息
     * 
     * @param id 飞书部门主键
     * @return 结果
     */
    @Override
    public int deleteSysFeishuDeptById(Long id)
    {
        return sysFeishuDeptMapper.deleteSysFeishuDeptById(id);
    }
}
