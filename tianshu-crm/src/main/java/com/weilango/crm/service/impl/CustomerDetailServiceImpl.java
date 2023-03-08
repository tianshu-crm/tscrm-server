package com.weilango.crm.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.crm.mapper.CustomerDetailMapper;
import com.weilango.crm.domain.CustomerDetail;
import com.weilango.crm.service.ICustomerDetailService;

/**
 * 线索详情Service业务层处理
 * 
 * @author hao
 * @date 2023-01-09
 */
@Service
public class CustomerDetailServiceImpl extends ServiceImpl<CustomerDetailMapper, CustomerDetail> implements ICustomerDetailService
{
    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    /**
     * 查询线索详情
     * 
     * @param id 线索详情主键
     * @return 线索详情
     */
    @Override
    public CustomerDetail selectCustomerDetailById(Long id)
    {
        return customerDetailMapper.selectCustomerDetailById(id);
    }

    /**
     * 查询线索详情列表
     * 
     * @param customerDetail 线索详情
     * @return 线索详情
     */
    @Override
    public List<CustomerDetail> selectCustomerDetailList(CustomerDetail customerDetail)
    {
        return customerDetailMapper.selectCustomerDetailList(customerDetail);
    }

    /**
     * 新增线索详情
     * 
     * @param customerDetail 线索详情
     * @return 结果
     */
    @Override
    public int insertCustomerDetail(CustomerDetail customerDetail)
    {
        customerDetail.setCreateTime(DateUtils.getNowDate());
        return customerDetailMapper.insertCustomerDetail(customerDetail);
    }

    /**
     * 修改线索详情
     * 
     * @param customerDetail 线索详情
     * @return 结果
     */
    @Override
    public int updateCustomerDetail(CustomerDetail customerDetail)
    {
        customerDetail.setUpdateTime(DateUtils.getNowDate());
        return customerDetailMapper.updateCustomerDetail(customerDetail);
    }

    /**
     * 批量删除线索详情
     * 
     * @param ids 需要删除的线索详情主键
     * @return 结果
     */
    @Override
    public int deleteCustomerDetailByIds(Long[] ids)
    {
        return customerDetailMapper.deleteCustomerDetailByIds(ids);
    }

    /**
     * 删除线索详情信息
     * 
     * @param id 线索详情主键
     * @return 结果
     */
    @Override
    public int deleteCustomerDetailById(Long id)
    {
        return customerDetailMapper.deleteCustomerDetailById(id);
    }
}
