package com.tianshu.crm.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.crm.domain.CustomerFailure;

/**
 * 线索战败Service接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface ICustomerFailureService extends IService<CustomerFailure>
{
    /**
     * 查询线索战败
     * 
     * @param id 线索战败主键
     * @return 线索战败
     */
    public CustomerFailure selectCustomerFailureById(Long id);

    /**
     * 查询线索战败列表
     * 
     * @param customerFailure 线索战败
     * @return 线索战败集合
     */
    public List<CustomerFailure> selectCustomerFailureList(CustomerFailure customerFailure);

    /**
     * 新增线索战败
     * 
     * @param customerFailure 线索战败
     * @return 结果
     */
    public int insertCustomerFailure(CustomerFailure customerFailure);

    /**
     * 修改线索战败
     * 
     * @param customerFailure 线索战败
     * @return 结果
     */
    public int updateCustomerFailure(CustomerFailure customerFailure);

    /**
     * 批量删除线索战败
     * 
     * @param ids 需要删除的线索战败主键集合
     * @return 结果
     */
    public int deleteCustomerFailureByIds(Long[] ids);

    /**
     * 删除线索战败信息
     * 
     * @param id 线索战败主键
     * @return 结果
     */
    public int deleteCustomerFailureById(Long id);
}
