package com.weilango.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.system.mapper.SysDingtalkRoleMapper;
import com.weilango.system.domain.SysDingtalkRole;
import com.weilango.system.service.ISysDingtalkRoleService;

/**
 * 钉钉角色Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Service
public class SysDingtalkRoleServiceImpl extends ServiceImpl<SysDingtalkRoleMapper, SysDingtalkRole> implements ISysDingtalkRoleService
{
    @Autowired
    private SysDingtalkRoleMapper sysDingtalkRoleMapper;

    /**
     * 查询钉钉角色
     * 
     * @param id 钉钉角色主键
     * @return 钉钉角色
     */
    @Override
    public SysDingtalkRole selectSysDingtalkRoleById(Long id)
    {
        return sysDingtalkRoleMapper.selectSysDingtalkRoleById(id);
    }

    /**
     * 查询钉钉角色列表
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 钉钉角色
     */
    @Override
    public List<SysDingtalkRole> selectSysDingtalkRoleList(SysDingtalkRole sysDingtalkRole)
    {
        return sysDingtalkRoleMapper.selectSysDingtalkRoleList(sysDingtalkRole);
    }

    /**
     * 新增钉钉角色
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 结果
     */
    @Override
    public int insertSysDingtalkRole(SysDingtalkRole sysDingtalkRole)
    {
        sysDingtalkRole.setCreateTime(DateUtils.getNowDate());
        return sysDingtalkRoleMapper.insertSysDingtalkRole(sysDingtalkRole);
    }

    /**
     * 修改钉钉角色
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 结果
     */
    @Override
    public int updateSysDingtalkRole(SysDingtalkRole sysDingtalkRole)
    {
        sysDingtalkRole.setUpdateTime(DateUtils.getNowDate());
        return sysDingtalkRoleMapper.updateSysDingtalkRole(sysDingtalkRole);
    }

    /**
     * 批量删除钉钉角色
     * 
     * @param ids 需要删除的钉钉角色主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkRoleByIds(Long[] ids)
    {
        return sysDingtalkRoleMapper.deleteSysDingtalkRoleByIds(ids);
    }

    /**
     * 删除钉钉角色信息
     * 
     * @param id 钉钉角色主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkRoleById(Long id)
    {
        return sysDingtalkRoleMapper.deleteSysDingtalkRoleById(id);
    }
}
