package com.weilango.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.po.SysMessage;

import java.util.List;


/**
 * 系统消息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
public interface SysMessageMapper extends BaseMapper<SysMessage>
{
    /**
     * 查询系统消息
     * 
     * @param id 系统消息主键
     * @return 系统消息
     */
    public SysMessage selectSysMessageById(Long id);

    /**
     * 查询系统消息列表
     * 
     * @param sysMessage 系统消息
     * @return 系统消息集合
     */
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage);

    /**
     * 新增系统消息
     * 
     * @param sysMessage 系统消息
     * @return 结果
     */
    public int insertSysMessage(SysMessage sysMessage);

    /**
     * 修改系统消息
     * 
     * @param sysMessage 系统消息
     * @return 结果
     */
    public int updateSysMessage(SysMessage sysMessage);

    /**
     * 删除系统消息
     * 
     * @param id 系统消息主键
     * @return 结果
     */
    public int deleteSysMessageById(Long id);

    /**
     * 批量删除系统消息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMessageByIds(Long[] ids);
}
