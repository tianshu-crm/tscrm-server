package com.tianshu.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.system.domain.SysFeishuDept;

/**
 * 飞书部门Mapper接口
 * 
 * @author hao
 * @date 2023-02-15
 */
public interface SysFeishuDeptMapper extends BaseMapper<SysFeishuDept>
{
    /**
     * 查询飞书部门
     * 
     * @param id 飞书部门主键
     * @return 飞书部门
     */
    public SysFeishuDept selectSysFeishuDeptById(Long id);

    /**
     * 查询飞书部门列表
     * 
     * @param sysFeishuDept 飞书部门
     * @return 飞书部门集合
     */
    public List<SysFeishuDept> selectSysFeishuDeptList(SysFeishuDept sysFeishuDept);

    /**
     * 新增飞书部门
     * 
     * @param sysFeishuDept 飞书部门
     * @return 结果
     */
    public int insertSysFeishuDept(SysFeishuDept sysFeishuDept);

    /**
     * 修改飞书部门
     * 
     * @param sysFeishuDept 飞书部门
     * @return 结果
     */
    public int updateSysFeishuDept(SysFeishuDept sysFeishuDept);

    /**
     * 删除飞书部门
     * 
     * @param id 飞书部门主键
     * @return 结果
     */
    public int deleteSysFeishuDeptById(Long id);

    /**
     * 批量删除飞书部门
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFeishuDeptByIds(Long[] ids);
}
