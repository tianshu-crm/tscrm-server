package com.tianshu.crm.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.utils.DateUtils;
import com.tianshu.crm.domain.CustomerTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.crm.mapper.CustomerTransferMapper;
import com.tianshu.crm.service.ICustomerTransferService;

/**
 * 线索转移Service业务层处理
 * 
 * @author hao
 * @date 2023-01-10
 */
@Service
public class CustomerTransferServiceImpl extends ServiceImpl<CustomerTransferMapper, CustomerTransfer> implements ICustomerTransferService
{
    @Autowired
    private CustomerTransferMapper customerTransferMapper;

    /**
     * 查询线索转移
     * 
     * @param id 线索转移主键
     * @return 线索转移
     */
    @Override
    public CustomerTransfer selectCustomerTransferById(Long id)
    {
        return customerTransferMapper.selectCustomerTransferById(id);
    }

    /**
     * 查询线索转移列表
     * 
     * @param customerTransfer 线索转移
     * @return 线索转移
     */
    @Override
    public List<CustomerTransfer> selectCustomerTransferList(CustomerTransfer customerTransfer)
    {
        return customerTransferMapper.selectCustomerTransferList(customerTransfer);
    }

    /**
     * 新增线索转移
     * 
     * @param customerTransfer 线索转移
     * @return 结果
     */
    @Override
    public int insertCustomerTransfer(CustomerTransfer customerTransfer)
    {
        customerTransfer.setCreateTime(DateUtils.getNowDate());
        return customerTransferMapper.insertCustomerTransfer(customerTransfer);
    }

    /**
     * 修改线索转移
     * 
     * @param customerTransfer 线索转移
     * @return 结果
     */
    @Override
    public int updateCustomerTransfer(CustomerTransfer customerTransfer)
    {
        customerTransfer.setUpdateTime(DateUtils.getNowDate());
        return customerTransferMapper.updateCustomerTransfer(customerTransfer);
    }

    /**
     * 批量删除线索转移
     * 
     * @param ids 需要删除的线索转移主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTransferByIds(Long[] ids)
    {
        return customerTransferMapper.deleteCustomerTransferByIds(ids);
    }

    /**
     * 删除线索转移信息
     * 
     * @param id 线索转移主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTransferById(Long id)
    {
        return customerTransferMapper.deleteCustomerTransferById(id);
    }
}
