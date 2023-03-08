package com.weilango.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.po.SysArea;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface SysAreaMapper extends BaseMapper<SysArea>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysArea selectSysAreaById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysArea 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysAreaById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAreaByIds(Long[] ids);
}
