package com.weilango.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weilango.system.domain.SysDingtalkUser;

/**
 * 钉钉用户Service接口
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
public interface ISysDingtalkUserService extends IService<SysDingtalkUser>
{
    /**
     * 查询钉钉用户
     * 
     * @param id 钉钉用户主键
     * @return 钉钉用户
     */
    public SysDingtalkUser selectSysDingtalkUserById(Long id);

    /**
     * 查询钉钉用户列表
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 钉钉用户集合
     */
    public List<SysDingtalkUser> selectSysDingtalkUserList(SysDingtalkUser sysDingtalkUser);

    /**
     * 新增钉钉用户
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 结果
     */
    public int insertSysDingtalkUser(SysDingtalkUser sysDingtalkUser);

    /**
     * 修改钉钉用户
     * 
     * @param sysDingtalkUser 钉钉用户
     * @return 结果
     */
    public int updateSysDingtalkUser(SysDingtalkUser sysDingtalkUser);

    /**
     * 批量删除钉钉用户
     * 
     * @param ids 需要删除的钉钉用户主键集合
     * @return 结果
     */
    public int deleteSysDingtalkUserByIds(Long[] ids);

    /**
     * 删除钉钉用户信息
     * 
     * @param id 钉钉用户主键
     * @return 结果
     */
    public int deleteSysDingtalkUserById(Long id);
}
