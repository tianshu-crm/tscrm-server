package com.weilango.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weilango.system.domain.SysDingtalkRole;

/**
 * 钉钉角色Service接口
 * 
 * @author ruoyi
 * @date 2022-12-30
 */
public interface ISysDingtalkRoleService extends IService<SysDingtalkRole>
{
    /**
     * 查询钉钉角色
     * 
     * @param id 钉钉角色主键
     * @return 钉钉角色
     */
    public SysDingtalkRole selectSysDingtalkRoleById(Long id);

    /**
     * 查询钉钉角色列表
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 钉钉角色集合
     */
    public List<SysDingtalkRole> selectSysDingtalkRoleList(SysDingtalkRole sysDingtalkRole);

    /**
     * 新增钉钉角色
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 结果
     */
    public int insertSysDingtalkRole(SysDingtalkRole sysDingtalkRole);

    /**
     * 修改钉钉角色
     * 
     * @param sysDingtalkRole 钉钉角色
     * @return 结果
     */
    public int updateSysDingtalkRole(SysDingtalkRole sysDingtalkRole);

    /**
     * 批量删除钉钉角色
     * 
     * @param ids 需要删除的钉钉角色主键集合
     * @return 结果
     */
    public int deleteSysDingtalkRoleByIds(Long[] ids);

    /**
     * 删除钉钉角色信息
     * 
     * @param id 钉钉角色主键
     * @return 结果
     */
    public int deleteSysDingtalkRoleById(Long id);
}
