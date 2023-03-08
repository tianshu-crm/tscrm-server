package com.weilango.crm.service.impl;

import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.core.domain.entity.SysUser;
import com.weilango.common.exception.ServiceException;
import com.weilango.common.utils.DateUtils;
import com.weilango.common.utils.SecurityUtils;
import com.weilango.common.utils.StringUtils;
import com.weilango.common.utils.bean.BeanValidators;
import com.weilango.crm.domain.Customer;
import com.weilango.crm.domain.vo.CustomerRegisterVo;
import com.weilango.crm.service.ICustomerService;
import com.weilango.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.crm.mapper.CustomerRegisterMapper;
import com.weilango.crm.domain.CustomerRegister;
import com.weilango.crm.service.ICustomerRegisterService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 来店登记Service业务层处理
 * 
 * @author hao
 * @date 2023-01-11
 */
@Service
public class CustomerRegisterServiceImpl extends ServiceImpl<CustomerRegisterMapper, CustomerRegister> implements ICustomerRegisterService
{
    @Autowired
    private CustomerRegisterMapper customerRegisterMapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISysUserService sysUserService;


    /**
     * 查询来店登记
     * 
     * @param id 来店登记主键
     * @return 来店登记
     */
    @Override
    public CustomerRegister selectCustomerRegisterById(Long id)
    {
        return customerRegisterMapper.selectCustomerRegisterById(id);
    }

    /**
     * 查询来店登记列表
     * 
     * @param customerRegister 来店登记
     * @return 来店登记
     */
    @Override
    public List<CustomerRegisterVo> selectCustomerRegisterList(CustomerRegister customerRegister)
    {
        return customerRegisterMapper.selectCustomerRegisterList(customerRegister);
    }

    /**
     * 新增来店登记
     * 
     * @param customerRegister 来店登记
     * @return 结果
     */
    @Override
    public void insertCustomerRegister(CustomerRegister customerRegister)
    {
        customerRegister.setCreateInfo(SecurityUtils.getUsername());
        customerRegisterMapper.insertCustomerRegister(customerRegister);
    }

    /**
     * 修改来店登记
     * 
     * @param customerRegister 来店登记
     * @return 结果
     */
    @Override
    public int updateCustomerRegister(CustomerRegister customerRegister)
    {
        customerRegister.setUpdateTime(DateUtils.getNowDate());
        return customerRegisterMapper.updateCustomerRegister(customerRegister);
    }

    /**
     * 批量删除来店登记
     * 
     * @param ids 需要删除的来店登记主键
     * @return 结果
     */
    @Override
    public int deleteCustomerRegisterByIds(Long[] ids)
    {
        return customerRegisterMapper.deleteCustomerRegisterByIds(ids);
    }

    /**
     * 删除来店登记信息
     * 
     * @param id 来店登记主键
     * @return 结果
     */
    @Override
    public int deleteCustomerRegisterById(Long id)
    {
        return customerRegisterMapper.deleteCustomerRegisterById(id);
    }

    @Override
    public String importData(List<CustomerRegisterVo> registerVoList) {

        if (StringUtils.isNull(registerVoList) || registerVoList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        if (registerVoList.size() >1000) {
            throw new ServiceException("导入最大限制1000条！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        for (CustomerRegisterVo registerVo : registerVoList) {
            try {
                CustomerRegister register = CustomerRegister.builder()
                        .name(registerVo.getName())
                        .phonenumber(registerVo.getPhonenumber())
                        .wechat(registerVo.getWechat())
                        .times(registerVo.getTimes())
                        .type(registerVo.getType())
                        .purpose(registerVo.getPurpose())
                        .intentionCarType(registerVo.getIntentionCarType())
                        .intentionCarSeries(registerVo.getIntentionCarSeries())
                        .referrer(registerVo.getReferrer())
                        .referrerPhone(registerVo.getReferrerPhone())
                        .referrerWechat(registerVo.getReferrerWechat())
                        .build();
                register.setCreateInfo(SecurityUtils.getUsername());
                if(ObjectUtil.isNotNull(registerVo.getPhonenumber())&&ObjectUtil.isNotEmpty(registerVo.getPhonenumber())){
                    List<Customer> customers = customerService.list(new LambdaQueryWrapper<Customer>()
                            .eq(Customer::getDelFlag,0)
                            .eq(Customer::getPhonenumber,registerVo.getPhonenumber())
                    );
                    if(customers.size()>0){
                        register.setCustomerId(customers.get(0).getId());
                    }
                }
                if(ObjectUtil.isNotNull(registerVo.getUserName())&&ObjectUtil.isNotEmpty(registerVo.getUserName())){
                    List<SysUser> sysUsers = sysUserService.list(new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getDelFlag,0)
                            .eq(SysUser::getUserName,registerVo.getUserName())
                    );
                    if (sysUsers.size()>0) {
                        register.setUserId(sysUsers.get(0).getUserId());
                    }
                }
                this.save(register);
                successNum++;
            }catch (Exception e){
                failureNum++;
                log.error("来店登记导入失败===========>", e);
            }
            //successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
            //failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
            //String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
            //failureMsg.append(msg + e.getMessage());
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
        return successMsg.toString();
    }
}
