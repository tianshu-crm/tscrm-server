package com.weilango.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.system.mapper.SysDingtalkUserMapper;
import com.weilango.system.domain.SysDingtalkUser;
import com.weilango.system.service.ISysDingtalkUserService;

/**
 * 钉钉用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
@Service
public class SysDingtalkUserServiceImpl extends ServiceImpl<SysDingtalkUserMapper, SysDingtalkUser> implements ISysDingtalkUserService
{
    @Autowired
    private SysDingtalkUserMapper sysDingtalkUserMapper;

    /**
     * 查询钉钉用户
     * 
     * @param id 钉钉用户主键
     * @return 钉钉用户
     */
    @Override
    public SysDingtalkUser selectSysDingtalkUserById(Long id)
    {
        return sysDingtalkUserMapper.selectSysDingtalkUserById(id);
    }

    /**
     * 查询钉钉用户列表
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 钉钉用户
     */
    @Override
    public List<SysDingtalkUser> selectSysDingtalkUserList(SysDingtalkUser sysDingtalkUser)
    {
        return sysDingtalkUserMapper.selectSysDingtalkUserList(sysDingtalkUser);
    }

    /**
     * 新增钉钉用户
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 结果
     */
    @Override
    public int insertSysDingtalkUser(SysDingtalkUser sysDingtalkUser)
    {
        sysDingtalkUser.setCreateTime(DateUtils.getNowDate());
        return sysDingtalkUserMapper.insertSysDingtalkUser(sysDingtalkUser);
    }

    /**
     * 修改钉钉用户
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 结果
     */
    @Override
    public int updateSysDingtalkUser(SysDingtalkUser sysDingtalkUser)
    {
        sysDingtalkUser.setUpdateTime(DateUtils.getNowDate());
        return sysDingtalkUserMapper.updateSysDingtalkUser(sysDingtalkUser);
    }

    /**
     * 批量删除钉钉用户
     * 
     * @param ids 需要删除的钉钉用户主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkUserByIds(Long[] ids)
    {
        return sysDingtalkUserMapper.deleteSysDingtalkUserByIds(ids);
    }

    /**
     * 删除钉钉用户信息
     * 
     * @param id 钉钉用户主键
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkUserById(Long id)
    {
        return sysDingtalkUserMapper.deleteSysDingtalkUserById(id);
    }
}
