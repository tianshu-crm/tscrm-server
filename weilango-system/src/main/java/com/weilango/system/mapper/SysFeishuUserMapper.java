package com.weilango.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.SysFeishuUser;

/**
 * 飞书用户Mapper接口
 * 
 * @author hao
 * @date 2023-02-15
 */
public interface SysFeishuUserMapper extends BaseMapper<SysFeishuUser>
{
    /**
     * 查询飞书用户
     * 
     * @param id 飞书用户主键
     * @return 飞书用户
     */
    public SysFeishuUser selectSysFeishuUserById(Long id);

    /**
     * 查询飞书用户列表
     * 
     * @param sysFeishuUser 飞书用户
     * @return 飞书用户集合
     */
    public List<SysFeishuUser> selectSysFeishuUserList(SysFeishuUser sysFeishuUser);

    /**
     * 新增飞书用户
     * 
     * @param sysFeishuUser 飞书用户
     * @return 结果
     */
    public int insertSysFeishuUser(SysFeishuUser sysFeishuUser);

    /**
     * 修改飞书用户
     * 
     * @param sysFeishuUser 飞书用户
     * @return 结果
     */
    public int updateSysFeishuUser(SysFeishuUser sysFeishuUser);

    /**
     * 删除飞书用户
     * 
     * @param id 飞书用户主键
     * @return 结果
     */
    public int deleteSysFeishuUserById(Long id);

    /**
     * 批量删除飞书用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFeishuUserByIds(Long[] ids);
}
