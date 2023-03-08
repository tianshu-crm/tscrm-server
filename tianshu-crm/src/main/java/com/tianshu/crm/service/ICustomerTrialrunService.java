package com.tianshu.crm.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.crm.domain.CustomerTrialrun;
import com.tianshu.crm.domain.vo.CustomerTrialrunVo;

/**
 * 试乘试驾Service接口
 * 
 * @author hao
 * @date 2023-01-11
 */
public interface ICustomerTrialrunService extends IService<CustomerTrialrun>
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
    public void insertCustomerTrialrun(CustomerTrialrun customerTrialrun);

    /**
     * 修改试乘试驾
     * 
     * @param customerTrialrun 试乘试驾
     * @return 结果
     */
    public int updateCustomerTrialrun(CustomerTrialrun customerTrialrun);

    /**
     * 批量删除试乘试驾
     * 
     * @param ids 需要删除的试乘试驾主键集合
     * @return 结果
     */
    public int deleteCustomerTrialrunByIds(Long[] ids);

    /**
     * 删除试乘试驾信息
     * 
     * @param id 试乘试驾主键
     * @return 结果
     */
    public int deleteCustomerTrialrunById(Long id);
}
