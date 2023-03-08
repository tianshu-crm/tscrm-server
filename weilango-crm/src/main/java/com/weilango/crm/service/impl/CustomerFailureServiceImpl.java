package com.weilango.crm.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.crm.mapper.CustomerFailureMapper;
import com.weilango.crm.domain.CustomerFailure;
import com.weilango.crm.service.ICustomerFailureService;

/**
 * 线索战败Service业务层处理
 * 
 * @author hao
 * @date 2023-01-10
 */
@Service
public class CustomerFailureServiceImpl extends ServiceImpl<CustomerFailureMapper, CustomerFailure> implements ICustomerFailureService
{
    @Autowired
    private CustomerFailureMapper customerFailureMapper;

    /**
     * 查询线索战败
     * 
     * @param id 线索战败主键
     * @return 线索战败
     */
    @Override
    public CustomerFailure selectCustomerFailureById(Long id)
    {
        return customerFailureMapper.selectCustomerFailureById(id);
    }

    /**
     * 查询线索战败列表
     * 
     * @param customerFailure 线索战败
     * @return 线索战败
     */
    @Override
    public List<CustomerFailure> selectCustomerFailureList(CustomerFailure customerFailure)
    {
        return customerFailureMapper.selectCustomerFailureList(customerFailure);
    }

    /**
     * 新增线索战败
     * 
     * @param customerFailure 线索战败
     * @return 结果
     */
    @Override
    public int insertCustomerFailure(CustomerFailure customerFailure)
    {
        customerFailure.setCreateTime(DateUtils.getNowDate());
        return customerFailureMapper.insertCustomerFailure(customerFailure);
    }

    /**
     * 修改线索战败
     * 
     * @param customerFailure 线索战败
     * @return 结果
     */
    @Override
    public int updateCustomerFailure(CustomerFailure customerFailure)
    {
        customerFailure.setUpdateTime(DateUtils.getNowDate());
        return customerFailureMapper.updateCustomerFailure(customerFailure);
    }

    /**
     * 批量删除线索战败
     * 
     * @param ids 需要删除的线索战败主键
     * @return 结果
     */
    @Override
    public int deleteCustomerFailureByIds(Long[] ids)
    {
        return customerFailureMapper.deleteCustomerFailureByIds(ids);
    }

    /**
     * 删除线索战败信息
     * 
     * @param id 线索战败主键
     * @return 结果
     */
    @Override
    public int deleteCustomerFailureById(Long id)
    {
        return customerFailureMapper.deleteCustomerFailureById(id);
    }
}
