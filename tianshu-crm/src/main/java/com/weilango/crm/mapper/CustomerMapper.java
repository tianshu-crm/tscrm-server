package com.weilango.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.crm.domain.Customer;
import com.weilango.crm.domain.vo.CustomerExportVo;
import com.weilango.crm.domain.vo.CustomerFailureVo;

import java.util.List;
import java.util.Map;

/**
 * 线索管理Mapper接口
 * 
 * @author hao
 * @date 2023-01-09
 */
public interface CustomerMapper extends BaseMapper<Customer>
{
    /**
     * 查询线索管理
     * 
     * @param id 线索管理主键
     * @return 线索管理
     */
    public Map<String,Object> selectCustomerById(Long id);

    /**
     * 查询线索管理列表
     * 
     * @param customer 线索管理
     * @return 线索管理集合
     */
    public List<CustomerExportVo> selectCustomerList(Customer customer);

    /**
     * 新增线索管理
     * 
     * @param customer 线索管理
     * @return 结果
     */
    public int insertCustomer(Customer customer);

    /**
     * 修改线索管理
     * 
     * @param customer 线索管理
     * @return 结果
     */
    public int updateCustomer(Customer customer);

    /**
     * 删除线索管理
     * 
     * @param id 线索管理主键
     * @return 结果
     */
    public int deleteCustomerById(Long id);

    /**
     * 批量删除线索管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerByIds(Long[] ids);

    /**
     * 战败池
     * @param failureVo
     * @return
     */
    public List<CustomerFailureVo> selectFailureList(CustomerFailureVo failureVo);

    /**
     * 战败详情
     * @param customerId
     * @return
     */
    public Map<String,Object> selectCustomerFailureById(Long customerId);

    /**
     * 去重列表
     * @param customer
     * @return
     */
    public List<CustomerExportVo> selectCustomerRepeatList(Customer customer);

    /**
     * 获取线索信息
     * @param id
     * @return
     */
    public Map<String,Object> customerInfo(Long id);
}
