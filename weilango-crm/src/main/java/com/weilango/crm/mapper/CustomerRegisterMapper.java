package com.weilango.crm.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.crm.domain.CustomerRegister;
import com.weilango.crm.domain.vo.CustomerRegisterVo;

/**
 * 来店登记Mapper接口
 * 
 * @author hao
 * @date 2023-01-11
 */
public interface CustomerRegisterMapper extends BaseMapper<CustomerRegister>
{
    /**
     * 查询来店登记
     * 
     * @param id 来店登记主键
     * @return 来店登记
     */
    public CustomerRegister selectCustomerRegisterById(Long id);

    /**
     * 查询来店登记列表
     * 
     * @param customerRegister 来店登记
     * @return 来店登记集合
     */
    public List<CustomerRegisterVo> selectCustomerRegisterList(CustomerRegister customerRegister);

    /**
     * 新增来店登记
     * 
     * @param customerRegister 来店登记
     * @return 结果
     */
    public int insertCustomerRegister(CustomerRegister customerRegister);

    /**
     * 修改来店登记
     * 
     * @param customerRegister 来店登记
     * @return 结果
     */
    public int updateCustomerRegister(CustomerRegister customerRegister);

    /**
     * 删除来店登记
     * 
     * @param id 来店登记主键
     * @return 结果
     */
    public int deleteCustomerRegisterById(Long id);

    /**
     * 批量删除来店登记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerRegisterByIds(Long[] ids);
}
