package com.weilango.crm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import com.weilango.common.utils.SecurityUtils;
import com.weilango.crm.domain.Customer;
import com.weilango.crm.domain.CustomerTrialrun;
import com.weilango.crm.domain.vo.CustomerTrialrunVo;
import com.weilango.crm.mapper.CustomerTrialrunMapper;
import com.weilango.crm.service.ICustomerService;
import com.weilango.crm.service.ICustomerTrialrunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 试乘试驾Service业务层处理
 * 
 * @author hao
 * @date 2023-01-11
 */
@Service
public class CustomerTrialrunServiceImpl extends ServiceImpl<CustomerTrialrunMapper, CustomerTrialrun> implements ICustomerTrialrunService
{
    @Autowired
    private CustomerTrialrunMapper customerTrialrunMapper;

    @Autowired
    private ICustomerService customerService;

    /**
     * 查询试乘试驾
     * 
     * @param id 试乘试驾主键
     * @return 试乘试驾
     */
    @Override
    public CustomerTrialrun selectCustomerTrialrunById(Long id)
    {
        return customerTrialrunMapper.selectCustomerTrialrunById(id);
    }

    /**
     * 查询试乘试驾列表
     * 
     * @param customerTrialrun 试乘试驾
     * @return 试乘试驾
     */
    @Override
    public List<CustomerTrialrunVo> selectCustomerTrialrunList(CustomerTrialrun customerTrialrun)
    {
        return customerTrialrunMapper.selectCustomerTrialrunList(customerTrialrun);
    }

    /**
     * 新增试乘试驾
     * 
     * @param customerTrialrun 试乘试驾
     * @return 结果
     */
    @Override
    public void insertCustomerTrialrun(CustomerTrialrun customerTrialrun)
    {
        customerTrialrun.setCreateInfo(SecurityUtils.getUsername());
        //试乘试驾单号
        String trialrunNo = "scsj-"+DateUtils.parseDateToStr("YYYYMMDDHHMMSS",new Date())+ RandomUtil.randomNumbers(4);
        customerTrialrun.setTrialrunNo(trialrunNo);
        /*List<Customer> customerList = customerService.list(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getDelFlag,0)
                .eq(Customer::getPhonenumber,customerTrialrun.getPhonenumber())
        );
        if(customerList.size()>0){
            customerTrialrun.setCustomerId(customerList.get(0).getId());
        }*/
        customerTrialrunMapper.insertCustomerTrialrun(customerTrialrun);
    }

    /**
     * 修改试乘试驾
     * 
     * @param customerTrialrun 试乘试驾
     * @return 结果
     */
    @Override
    public int updateCustomerTrialrun(CustomerTrialrun customerTrialrun)
    {
        customerTrialrun.setUpdateInfo(SecurityUtils.getUsername());
        if(ObjectUtil.isEmpty(customerTrialrun.getTrialrunNo())){
            String trialrunNo = "scsj-"+DateUtils.parseDateToStr("YYYYMMDDHHMMSS",new Date())+ RandomUtil.randomNumbers(4);
            customerTrialrun.setTrialrunNo(trialrunNo);
        }
        return customerTrialrunMapper.updateCustomerTrialrun(customerTrialrun);
    }

    /**
     * 批量删除试乘试驾
     * 
     * @param ids 需要删除的试乘试驾主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTrialrunByIds(Long[] ids)
    {
        return customerTrialrunMapper.deleteCustomerTrialrunByIds(ids);
    }

    /**
     * 删除试乘试驾信息
     * 
     * @param id 试乘试驾主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTrialrunById(Long id)
    {
        return customerTrialrunMapper.deleteCustomerTrialrunById(id);
    }
}
