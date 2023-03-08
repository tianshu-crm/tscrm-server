package com.weilango.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.SysDingtalkDept;

/**
 * 钉钉部门Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
public interface SysDingtalkDeptMapper extends BaseMapper<SysDingtalkDept>
{
    /**
     * 查询钉钉部门
     * 
     * @param id 钉钉部门主键
     * @return 钉钉部门
     */
    public SysDingtalkDept selectSysDingtalkDeptById(Long id);

    /**
     * 查询钉钉部门列表
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 钉钉部门集合
     */
    public List<SysDingtalkDept> selectSysDingtalkDeptList(SysDingtalkDept sysDingtalkDept);

    /**
     * 新增钉钉部门
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 结果
     */
    public int insertSysDingtalkDept(SysDingtalkDept sysDingtalkDept);

    /**
     * 修改钉钉部门
     * 
     * @param sysDingtalkDept 钉钉部门
     * @return 结果
     */
    public int updateSysDingtalkDept(SysDingtalkDept sysDingtalkDept);

    /**
     * 删除钉钉部门
     * 
     * @param id 钉钉部门主键
     * @return 结果
     */
    public int deleteSysDingtalkDeptById(Long id);

    /**
     * 批量删除钉钉部门
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysDingtalkDeptByIds(Long[] ids);
}
