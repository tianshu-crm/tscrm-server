package com.tianshu.crm.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.crm.domain.CustomerLevel;

/**
 * 线索级别调整Mapper接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface CustomerLevelMapper extends BaseMapper<CustomerLevel>
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
     * 删除线索级别调整
     * 
     * @param id 线索级别调整主键
     * @return 结果
     */
    public int deleteCustomerLevelById(Long id);

    /**
     * 批量删除线索级别调整
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerLevelByIds(Long[] ids);
}
