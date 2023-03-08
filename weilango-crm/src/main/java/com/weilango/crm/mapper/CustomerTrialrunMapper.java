package com.weilango.crm.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.crm.domain.CustomerTrialrun;
import com.weilango.crm.domain.vo.CustomerTrialrunVo;

/**
 * 试乘试驾Mapper接口
 * 
 * @author hao
 * @date 2023-01-11
 */
public interface CustomerTrialrunMapper extends BaseMapper<CustomerTrialrun>
{
    /**
     * 查询试乘试驾
     * 
     * @param id 试乘试驾主键
     * @return 试乘试驾
     */
    public CustomerTrialrun selectCustomerTrialrunById(Long id);

    /**
     * 查询试乘试驾列表
     * 
     * @param customerTrialrun 试乘试驾
     * @return 试乘试驾集合
     */
    public List<CustomerTrialrunVo> selectCustomerTrialrunList(CustomerTrialrun customerTrialrun);

    /**
     * 新增试乘试驾
     * 
     * @param customerTrialrun 试乘试驾
     * @return 结果
     */
    public int insertCustomerTrialrun(CustomerTrialrun customerTrialrun);

    /**
     * 修改试乘试驾
     * 
     * @param customerTrialrun 试乘试驾
     * @return 结果
     */
    public int updateCustomerTrialrun(CustomerTrialrun customerTrialrun);

    /**
     * 删除试乘试驾
     * 
     * @param id 试乘试驾主键
     * @return 结果
     */
    public int deleteCustomerTrialrunById(Long id);

    /**
     * 批量删除试乘试驾
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerTrialrunByIds(Long[] ids);
}
