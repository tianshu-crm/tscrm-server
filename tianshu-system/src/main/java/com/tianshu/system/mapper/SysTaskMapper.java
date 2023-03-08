package com.tianshu.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.system.domain.po.SysTask;


/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hao
 * @date 2023-01-13
 */
public interface SysTaskMapper extends BaseMapper<SysTask>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysTask selectSysTaskById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysTask 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysTask> selectSysTaskList(SysTask sysTask);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysTask 【请填写功能名称】
     * @return 结果
     */
    public int insertSysTask(SysTask sysTask);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysTask 【请填写功能名称】
     * @return 结果
     */
    public int updateSysTask(SysTask sysTask);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysTaskById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTaskByIds(Long[] ids);
}
