package com.weilango.crm.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.crm.mapper.CustomerLevelMapper;
import com.weilango.crm.domain.CustomerLevel;
import com.weilango.crm.service.ICustomerLevelService;

/**
 * 线索级别调整Service业务层处理
 * 
 * @author hao
 * @date 2023-01-10
 */
@Service
public class CustomerLevelServiceImpl extends ServiceImpl<CustomerLevelMapper, CustomerLevel> implements ICustomerLevelService
{
    @Autowired
    private CustomerLevelMapper customerLevelMapper;

    /**
     * 查询线索级别调整
     * 
     * @param id 线索级别调整主键
     * @return 线索级别调整
     */
    @Override
    public CustomerLevel selectCustomerLevelById(Long id)
    {
        return customerLevelMapper.selectCustomerLevelById(id);
    }

    /**
     * 查询线索级别调整列表
     * 
     * @param customerLevel 线索级别调整
     * @return 线索级别调整
     */
    @Override
    public List<CustomerLevel> selectCustomerLevelList(CustomerLevel customerLevel)
    {
        return customerLevelMapper.selectCustomerLevelList(customerLevel);
    }

    /**
     * 新增线索级别调整
     * 
     * @param customerLevel 线索级别调整
     * @return 结果
     */
    @Override
    public int insertCustomerLevel(CustomerLevel customerLevel)
    {
        customerLevel.setCreateTime(DateUtils.getNowDate());
        return customerLevelMapper.insertCustomerLevel(customerLevel);
    }

    /**
     * 修改线索级别调整
     * 
     * @param customerLevel 线索级别调整
     * @return 结果
     */
    @Override
    public int updateCustomerLevel(CustomerLevel customerLevel)
    {
        customerLevel.setUpdateTime(DateUtils.getNowDate());
        return customerLevelMapper.updateCustomerLevel(customerLevel);
    }

    /**
     * 批量删除线索级别调整
     * 
     * @param ids 需要删除的线索级别调整主键
     * @return 结果
     */
    @Override
    public int deleteCustomerLevelByIds(Long[] ids)
    {
        return customerLevelMapper.deleteCustomerLevelByIds(ids);
    }

    /**
     * 删除线索级别调整信息
     * 
     * @param id 线索级别调整主键
     * @return 结果
     */
    @Override
    public int deleteCustomerLevelById(Long id)
    {
        return customerLevelMapper.deleteCustomerLevelById(id);
    }
}
