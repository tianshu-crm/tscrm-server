package com.tianshu.crm.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.crm.domain.CustomerDetail;

/**
 * 线索详情Mapper接口
 * 
 * @author hao
 * @date 2023-01-09
 */
public interface CustomerDetailMapper extends BaseMapper<CustomerDetail>
{
    /**
     * 查询线索详情
     * 
     * @param id 线索详情主键
     * @return 线索详情
     */
    public CustomerDetail selectCustomerDetailById(Long id);

    /**
     * 查询线索详情列表
     * 
     * @param customerDetail 线索详情
     * @return 线索详情集合
     */
    public List<CustomerDetail> selectCustomerDetailList(CustomerDetail customerDetail);

    /**
     * 新增线索详情
     * 
     * @param customerDetail 线索详情
     * @return 结果
     */
    public int insertCustomerDetail(CustomerDetail customerDetail);

    /**
     * 修改线索详情
     * 
     * @param customerDetail 线索详情
     * @return 结果
     */
    public int updateCustomerDetail(CustomerDetail customerDetail);

    /**
     * 删除线索详情
     * 
     * @param id 线索详情主键
     * @return 结果
     */
    public int deleteCustomerDetailById(Long id);

    /**
     * 批量删除线索详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerDetailByIds(Long[] ids);
}
