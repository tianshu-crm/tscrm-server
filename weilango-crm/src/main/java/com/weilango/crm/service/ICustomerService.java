package com.weilango.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilango.crm.domain.Customer;
import com.weilango.crm.domain.CustomerFailure;
import com.weilango.crm.domain.CustomerLevel;
import com.weilango.crm.domain.CustomerTransfer;
import com.weilango.crm.domain.dto.CustomerAddDto;
import com.weilango.crm.domain.vo.CustomerExportDto;
import com.weilango.crm.domain.vo.CustomerExportVo;
import com.weilango.crm.domain.vo.CustomerFailureVo;
import com.weilango.crm.domain.vo.CustomerRepeatVo;

import java.util.List;
import java.util.Map;

/**
 * 线索管理Service接口
 * 
 * @author hao
 * @date 2023-01-09
 */
public interface ICustomerService extends IService<Customer>
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
    public void insertCustomer(CustomerAddDto customer);

    /**
     * 修改线索管理
     * 
     * @param customer 线索管理
     * @return 结果
     */
    public void updateCustomer(CustomerAddDto customer);

    /**
     * 批量删除线索管理
     * 
     * @param ids 需要删除的线索管理主键集合
     * @return 结果
     */
    public int deleteCustomerByIds(Long[] ids);

    /**
     * 删除线索管理信息
     * 
     * @param id 线索管理主键
     * @return 结果
     */
    public int deleteCustomerById(Long id);


    /**
     * 批量分配顾问
     * @param userId
     * @param customerIds
     */
    public void allotUser(Long userId,Long[] customerIds,String nextTime);

    /**
     * 取消分配
     * @param customerId
     */
    public void cancel(Long customerId);

    /**
     * 线索转移
     * @param transfer
     */
    public void customerTransfer(CustomerTransfer transfer);

    /**
     * 调级
     * @param level
     */
    public void customerLevel(CustomerLevel level);

    /**
     * 战败
     * @param failure
     */
    public void customerFailure(CustomerFailure failure);

    /**
     * 审批
     * @param taskId
     * @param approvalStatus
     * @param approvalLeaveMsg
     */
    public void customerAudit(Long taskId,String approvalStatus,String approvalLeaveMsg);


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
     * 激活
     * @param
     */
    public void customerActivate(Long[] failureIds,String activateReason);

    /**
     * 导入数据
     * @param customerVoList
     * @return
     */
    public String importData(List<CustomerExportDto> customerVoList,String importType);

    /**
     * 线索去重列表
     * @param customer
     * @return
     */
    public List<CustomerRepeatVo> selectCustomerRepeatList(Customer customer);

    /**
     * 替换数据
     * @param customerIds
     */
    public void repeat(Long [] customerIds);

    /**
     * 删除数据
     * @param customerIds
     */
    public void repeatDelete(Long [] customerIds);

    /**
     * 获起用户信息
     * @param id
     * @return
     */
    public Map<String,Object> customerInfo(Long id);
}
