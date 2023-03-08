package com.tianshu.crm.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.crm.domain.CustomerLevel;

/**
 * 线索级别调整Service接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface ICustomerLevelService extends IService<CustomerLevel>
{
    /**
     * 查询线索级别调整
     * 
     * @param id 线索级别调整主键
     * @return 线索级别调整
     */
    public CustomerLevel selectCustomerLevelById(Long id);

    /**
     * 查询线索级别调整列表
     * 
     * @param customerLevel 线索级别调整
     * @return 线索级别调整集合
     */
    public List<CustomerLevel> selectCustomerLevelList(CustomerLevel customerLevel);

    /**
     * 新增线索级别调整
     * 
     * @param customerLevel 线索级别调整
     * @return 结果
     */
    public int insertCustomerLevel(CustomerLevel customerLevel);

    /**
     * 修改线索级别调整
     * 
     * @param customerLevel 线索级别调整
     * @return 结果
     */
    public int updateCustomerLevel(CustomerLevel customerLevel);

    /**
     * 批量删除线索级别调整
     * 
     * @param ids 需要删除的线索级别调整主键集合
     * @return 结果
     */
    public int deleteCustomerLevelByIds(Long[] ids);

    /**
     * 删除线索级别调整信息
     * 
     * @param id 线索级别调整主键
     * @return 结果
     */
    public int deleteCustomerLevelById(Long id);
}
