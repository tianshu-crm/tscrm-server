package com.tianshu.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.system.mapper.SysFeishuUserMapper;
import com.tianshu.system.domain.SysFeishuUser;
import com.tianshu.system.service.ISysFeishuUserService;

/**
 * 飞书用户Service业务层处理
 * 
 * @author hao
 * @date 2023-02-15
 */
@Service
public class SysFeishuUserServiceImpl extends ServiceImpl<SysFeishuUserMapper, SysFeishuUser> implements ISysFeishuUserService
{
    @Autowired
    private SysFeishuUserMapper sysFeishuUserMapper;

    /**
     * 查询飞书用户
     * 
     * @param id 飞书用户主键
     * @return 飞书用户
     */
    @Override
    public SysFeishuUser selectSysFeishuUserById(Long id)
    {
        return sysFeishuUserMapper.selectSysFeishuUserById(id);
    }

    /**
     * 查询飞书用户列表
     * 
     * @param sysFeishuUser 飞书用户
     * @return 飞书用户
     */
    @Override
    public List<SysFeishuUser> selectSysFeishuUserList(SysFeishuUser sysFeishuUser)
    {
        return sysFeishuUserMapper.selectSysFeishuUserList(sysFeishuUser);
    }

    /**
     * 新增飞书用户
     * 
     * @param sysFeishuUser 飞书用户
     * @return 结果
     */
    @Override
    public int insertSysFeishuUser(SysFeishuUser sysFeishuUser)
    {
        sysFeishuUser.setCreateTime(DateUtils.getNowDate());
        return sysFeishuUserMapper.insertSysFeishuUser(sysFeishuUser);
    }

    /**
     * 修改飞书用户
     * 
     * @param sysFeishuUser 飞书用户
     * @return 结果
     */
    @Override
    public int updateSysFeishuUser(SysFeishuUser sysFeishuUser)
    {
        sysFeishuUser.setUpdateTime(DateUtils.getNowDate());
        return sysFeishuUserMapper.updateSysFeishuUser(sysFeishuUser);
    }

    /**
     * 批量删除飞书用户
     * 
     * @param ids 需要删除的飞书用户主键
     * @return 结果
     */
    @Override
    public int deleteSysFeishuUserByIds(Long[] ids)
    {
        return sysFeishuUserMapper.deleteSysFeishuUserByIds(ids);
    }

    /**
     * 删除飞书用户信息
     * 
     * @param id 飞书用户主键
     * @return 结果
     */
    @Override
    public int deleteSysFeishuUserById(Long id)
    {
        return sysFeishuUserMapper.deleteSysFeishuUserById(id);
    }
}
