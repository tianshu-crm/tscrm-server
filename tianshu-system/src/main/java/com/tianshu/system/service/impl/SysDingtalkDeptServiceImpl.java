package com.tianshu.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.system.mapper.SysDingtalkDeptMapper;
import com.tianshu.system.domain.SysDingtalkDept;
import com.tianshu.system.service.ISysDingtalkDeptService;

/**
 * 钉钉部门Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Service
public class SysDingtalkDeptServiceImpl extends ServiceImpl<SysDingtalkDeptMapper, SysDingtalkDept> implements ISysDingtalkDeptService
{
    @Autowired
    private SysDingtalkDeptMapper sysDingtalkDeptMapper;

    /**
     * 查询钉钉部门
     * 
     * @param id 钉钉部门主键
     * @return 钉钉部门
     */
    @Override
    public SysDingtalkDept selectSysDingtalkDeptById(Long id)
    {
        return sysDingtalkDeptMapper.selectSysDingtalkDeptById(id);
    }

    /**
     * 查询钉钉部门列表
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 钉钉部门
     */
    @Override
    public List<SysDingtalkDept> selectSysDingtalkDeptList(SysDingtalkDept sysDingtalkDept)
    {
        return sysDingtalkDeptMapper.selectSysDingtalkDeptList(sysDingtalkDept);
    }

    /**
     * 新增钉钉部门
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 结果
     */
    @Override
    public int insertSysDingtalkDept(SysDingtalkDept sysDingtalkDept)
    {
        sysDingtalkDept.setCreateTime(DateUtils.getNowDate());
        return sysDingtalkDeptMapper.insertSysDingtalkDept(sysDingtalkDept);
    }

    /**
     * 修改钉钉部门
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 结果
     */
    @Override
    public int updateSysDingtalkDept(SysDingtalkDept sysDingtalkDept)
    {
        sysDingtalkDept.setUpdateTime(DateUtils.getNowDate());
        return sysDingtalkDeptMapper.updateSysDingtalkDept(sysDingtalkDept);
    }

    /**
     * 批量删除钉钉部门
     * 
     * @param ids 需要删除的钉钉部门主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkDeptByIds(Long[] ids)
    {
        return sysDingtalkDeptMapper.deleteSysDingtalkDeptByIds(ids);
    }

    /**
     * 删除钉钉部门信息
     * 
     * @param id 钉钉部门主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkDeptById(Long id)
    {
        return sysDingtalkDeptMapper.deleteSysDingtalkDeptById(id);
    }

}
