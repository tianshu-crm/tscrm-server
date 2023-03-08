package com.weilango.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.model.LiveChannelListing;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.exception.ServiceException;
import com.weilango.common.utils.DateUtils;
import com.weilango.common.utils.SecurityUtils;
import com.weilango.common.utils.StringUtils;
import com.weilango.crm.domain.*;
import com.weilango.crm.domain.dto.CustomerAddDto;
import com.weilango.crm.domain.vo.CustomerExportDto;
import com.weilango.crm.domain.vo.CustomerExportVo;
import com.weilango.crm.domain.vo.CustomerFailureVo;
import com.weilango.crm.domain.vo.CustomerRepeatVo;
import com.weilango.crm.mapper.CustomerMapper;
import com.weilango.crm.service.*;
import com.weilango.system.domain.po.SysMessage;
import com.weilango.system.domain.po.SysTask;
import com.weilango.system.service.ISysMessageService;
import com.weilango.system.service.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 线索管理Service业务层处理
 * 
 * @author hao
 * @date 2023-01-09
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService
{
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ICustomerDetailService customerDetailService;

    @Autowired
    private ICustomerTrackService customerTrackService;

    @Autowired
    private ICustomerTransferService customerTransferService;

    @Autowired
    private ICustomerLevelService customerLevelService;

    @Autowired
    private ICustomerFailureService customerFailureService;

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private ISysTaskService sysTaskService;

    @Autowired
    private ICustomerService customerService;

    /**
     * 查询线索管理
     * 
     * @param id 线索管理主键
     * @return 线索管理
     */
    @Override
    public Map<String,Object> selectCustomerById(Long id)
    {
        return customerMapper.selectCustomerById(id);
    }

    /**
     * 查询线索管理列表
     * 
     * @param customer 线索管理
     * @return 线索管理
     */
    @Override
    public List<CustomerExportVo> selectCustomerList(Customer customer)
    {
        return customerMapper.selectCustomerList(customer);
    }

    /**
     * 新增线索管理
     * 
     * @param dto 线索管理
     * @return 结果
     */
    @Override
    @Transactional
    public void insertCustomer(CustomerAddDto dto)
    {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .phonenumber(dto.getPhonenumber())
                .wechat(dto.getWechat())
                .sex(dto.getSex())
                .age(dto.getAge())
                .level(dto.getLevel())
                .nature(dto.getNature())
                .businessType(dto.getBusinessType())
                .referrer(dto.getReferrer())
                .referrerPhone(dto.getReferrerPhone())
                .referrerWechat(dto.getReferrerWechat())
                .source("XIAN_SUO_LU_RU")
                .status("0")
                .build();
        customer.setCreateInfo(SecurityUtils.getUsername());
        customerMapper.insertCustomer(customer);

        CustomerDetail customerDetail = CustomerDetail.builder()
                .customerId(customer.getId())
                .intentionCarSeries(dto.getIntentionCarSeries())
                .intentionCarType(dto.getIntentionCarType())
                .purchaseCarType(dto.getPurchaseCarType())
                .purchaseCarBudget(dto.getPurchaseCarBudget())
                .purchaseCarTime(dto.getPurchaseCarTime())
                .isOnlineCarUser(dto.getIsOnlineCarUser())
                .intentionDesc(dto.getIntentionDesc())
                .isCertificate(dto.getIsCertificate())
                .isFile(dto.getIsFile())
                .certificateType(dto.getCertificateType())
                .certificateNo(dto.getCertificateNo())
                .build();
        customerDetail.setCreateInfo(SecurityUtils.getUsername());
        customerDetailService.save(customerDetail);
    }

    /**
     * 修改线索管理
     *
     * @param dto 线索管理
     */
    @Override
    @Transactional
    public void updateCustomer(CustomerAddDto dto)
    {
        //基本信息
        Customer customer = this.getById(dto.getCustomerId());
        customer.setName(dto.getName());
        customer.setPhonenumber(dto.getPhonenumber());
        customer.setWechat(dto.getWechat());
        customer.setAge(dto.getAge());
        customer.setLevel(dto.getLevel());
        customer.setNature(dto.getNature());
        customer.setBusinessType(dto.getBusinessType());
        customer.setReferrer(dto.getReferrer());
        customer.setReferrerPhone(dto.getReferrerPhone());
        customer.setReferrerWechat(dto.getReferrerWechat());
        customer.setUpdateBy(SecurityUtils.getUsername());
        customer.setSource(dto.getSource());
        customer.setStage(dto.getStage());
        customer.setStatus("2");
        if (ObjectUtil.isNull(customer.getUserId())){
            customer.setUserId(SecurityUtils.getUserId());
        }
        customerMapper.updateCustomer(customer);

        //详情
        CustomerDetail customerDetail = customerDetailService.getOne(new LambdaQueryWrapper<CustomerDetail>()
                .eq(CustomerDetail::getCustomerId,customer.getId())
        );

        customerDetail.setIntentionDesc(dto.getIntentionDesc());
        customerDetail.setIntentionCarSeries(dto.getIntentionCarSeries());
        customerDetail.setIntentionCarType(dto.getIntentionCarType());
        customerDetail.setPurchaseCarBudget(dto.getPurchaseCarBudget());
        customerDetail.setPurchaseCarType(dto.getPurchaseCarType());
        customerDetail.setPurchaseCarTime(dto.getPurchaseCarTime());
        customerDetail.setIsCertificate(dto.getIsCertificate());
        customerDetail.setIsOnlineCarUser(dto.getIsOnlineCarUser());
        customerDetail.setDesc(dto.getDesc());
        customerDetail.setCompeteCar(dto.getCompeteCar());
        customerDetail.setCompeteCompany(dto.getCompeteCompany());
        customerDetail.setCompeteDesc(dto.getCompeteDesc());
        customerDetail.setUpdateInfo(SecurityUtils.getUsername());
        customerDetail.setCertificateNo(dto.getCertificateNo());
        customerDetail.setCertificateType(dto.getCertificateType());
        customerDetail.setIsFile(dto.getIsFile());
        customerDetailService.saveOrUpdate(customerDetail);

        //线索轨迹
        if (!ObjectUtil.isAllEmpty(dto.getCallDuration(),dto.getCallDuration(),dto.getContent(),dto.getNextContent(),dto.getType(),dto.getNextTime())){
            CustomerTrack track = CustomerTrack.builder().build();
            track.setCustomerId(dto.getCustomerId());
            track.setCallDuration(dto.getCallDuration());
            track.setContent(dto.getContent());
            track.setNextContent(dto.getNextContent());
            track.setType(dto.getType());
            track.setNextTime(dto.getNextTime());
            track.setCreateInfo(SecurityUtils.getUsername());
            customerTrackService.save(track);
        }

    }

    /**
     * 批量删除线索管理
     * 
     * @param ids 需要删除的线索管理主键
     * @return 结果
     */
    @Override
    public int deleteCustomerByIds(Long[] ids)
    {
        return customerMapper.deleteCustomerByIds(ids);
    }

    /**
     * 删除线索管理信息
     * 
     * @param id 线索管理主键
     * @return 结果
     */
    @Override
    public int deleteCustomerById(Long id)
    {
        return customerMapper.deleteCustomerById(id);
    }

    @Override
    public void allotUser(Long userId, Long[] customerIds, String nextTime) {
        this.update(new LambdaUpdateWrapper<Customer>()
                .in(Customer::getId, Convert.toList(Long.class,customerIds))
                .set(Customer::getUserId,userId)
                .set(Customer::getStatus,"1")
                .set(Customer::getUpdateBy,SecurityUtils.getUsername())
                .set(Customer::getUpdateTime,DateUtils.getNowDate())
                .set(Customer::getAllotTime,DateUtils.getNowDate())
                .set(Customer::getNextTime, DateUtils.parseDate(nextTime))
        );
    }

    @Override
    public void cancel(Long customerId) {
        this.update(new LambdaUpdateWrapper<Customer>()
                .eq(Customer::getId,customerId)
                .set(Customer::getUserId,null)
                .set(Customer::getStatus,"0")
        );
    }

    @Override
    @Transactional
    public void customerTransfer(CustomerTransfer transfer) {
        Long[] customerIds = Convert.toLongArray(transfer.getCustomerIds());
        for (Long customerId : customerIds) {
            Customer customer = this.getById(customerId);
            transfer.setOrgUserId(customer.getUserId());
            customer.setUserId(transfer.getUserId());
            customer.setUpdateInfo(SecurityUtils.getUsername());
            customer.setNextTime(transfer.getNextTime());
            customer.setAllotTime(DateUtils.getNowDate());
            this.saveOrUpdate(customer);
            transfer.setAuditStatus("1");
            transfer.setCreateInfo(SecurityUtils.getUsername());
            transfer.setCustomerId(customerId);
            customerTransferService.insertCustomerTransfer(transfer);
        }
    }


    @Override
    @Transactional
    public void customerLevel(CustomerLevel level) {
        Customer customer = this.getById(level.getCustomerId());
        level.setOrgLevel(customer.getLevel());
        level.setAuditStatus("0");
        level.setCreateInfo(SecurityUtils.getUsername());
        customerLevelService.save(level);

        SysMessage sysMessage = new SysMessage();
        String title = SecurityUtils.getUsername()+"向你申请线索"+customer.getName()+"意向客户级别调整审批";
        sysMessage.setTitle(title);
        sysMessage.setBusinessType("2");
        sysMessage.setUserId(SecurityUtils.getUserId()+"");
        sysMessage.setType("3");
        sysMessageService.sendMesage(sysMessage);
        //发送审批任务
        SysTask sysTask = new SysTask();
        sysTask.setTitle(title);
        sysTask.setType("3");
        sysTask.setBusinessType("2");
        sysTask.setBusinessId(level.getId());
        JSONObject obj = new JSONObject();
        Map<String, Object> resultMap = customerService.customerInfo(customer.getId());
        obj.put("name",Convert.toStr(resultMap.get("name")));
        obj.put("sex",Convert.toStr(resultMap.get("sex")));
        obj.put("age",Convert.toStr(resultMap.get("age")));
        obj.put("orgLevel",Convert.toStr(resultMap.get("level")));
        obj.put("level",Convert.toStr(resultMap.get("level")));
        obj.put("userName",Convert.toStr(resultMap.get("userName")));
        obj.put("wechat",Convert.toStr(resultMap.get("wechat")));
        obj.put("phonenumber",Convert.toStr(resultMap.get("phonenumber")));
        obj.put("businessType",Convert.toStr(resultMap.get("businessType")));
        obj.put("nature",Convert.toStr(resultMap.get("nature")));
        obj.put("reason",Convert.toStr(level.getReason()));
        obj.put("customerId",Convert.toLong(resultMap.get("customerId")));
        sysTask.setDetail(obj.toString());
        sysTaskService.sendTask(sysTask);
    }

    @Override
    @Transactional
    public void customerFailure(CustomerFailure failure) {
        Customer customer = this.getById(failure.getCustomerId());
        failure.setAuditStatus("0");
        failure.setOrgUserId(customer.getUserId());
        failure.setCreateInfo(SecurityUtils.getUsername());
        customerFailureService.save(failure);
        SysMessage sysMessage = new SysMessage();
        String title = SecurityUtils.getUsername()+"向你申请线索"+customer.getName()+"战败审批";
        sysMessage.setTitle(title);
        sysMessage.setBusinessType("ZHAN_BAI_SHEN_PI");
        sysMessage.setUserId(SecurityUtils.getUserId()+"");
        sysMessage.setType("3");
        sysMessageService.sendMesage(sysMessage);
        //  发送审批任务
        SysTask sysTask = new SysTask();
        sysTask.setTitle(title);
        sysTask.setType("3");
        sysTask.setBusinessType("1");
        sysTask.setBusinessId(failure.getId());
        JSONObject obj = new JSONObject();
        Map<String, Object> resultMap = customerService.customerInfo(customer.getId());
        obj.put("name",Convert.toStr(resultMap.get("name")));
        obj.put("sex",Convert.toStr(resultMap.get("sex")));
        obj.put("age",Convert.toStr(resultMap.get("age")));
        obj.put("orgLevel",Convert.toStr(resultMap.get("level")));
        obj.put("userName",Convert.toStr(resultMap.get("userName")));
        obj.put("wechat",Convert.toStr(resultMap.get("wechat")));
        obj.put("phonenumber",Convert.toStr(resultMap.get("phonenumber")));
        obj.put("businessType",Convert.toStr(resultMap.get("businessType")));
        obj.put("nature",Convert.toStr(resultMap.get("nature")));
        obj.put("reason",Convert.toStr(failure.getReason()));
        obj.put("customerId",Convert.toLong(resultMap.get("customerId")));
        sysTask.setDetail(obj.toString());
        sysTaskService.sendTask(sysTask);
    }

    @Override
    @Transactional
    public void customerAudit(Long taskId,String approvalStatus,String approvalLeaveMsg) {
        SysTask task = sysTaskService.getById(taskId);

        if("1".equals(task.getBusinessType())){//战败
            CustomerFailure customerFailure = customerFailureService.getById(task.getBusinessId());
            customerFailure.setAuditStatus(approvalStatus);
            customerFailure.setActivateReason(approvalLeaveMsg);
            customerFailure.setUpdateInfo(SecurityUtils.getUsername());
            customerFailureService.saveOrUpdate(customerFailure);
            if("1".equals(approvalStatus)){
                Customer customer = this.getById(customerFailure.getCustomerId());
                customer.setStatus("4");
                customer.setUserId(null);
                customer.setNextTime(null);
                customer.setAllotTime(null);
                customer.setUpdateInfo(SecurityUtils.getUsername());
                this.saveOrUpdate(customer);
            }
        }else if("2".equals(task.getBusinessType())){//调级
            CustomerLevel customerLevel = customerLevelService.getById(task.getBusinessId());
            customerLevel.setAuditStatus(approvalStatus);
            customerLevel.setUpdateInfo(SecurityUtils.getUsername());
            customerLevelService.saveOrUpdate(customerLevel);
            if("1".equals(approvalStatus)){
                Customer customer = this.getById(customerLevel.getCustomerId());
                customer.setLevel(customerLevel.getLevel());
                customer.setNextTime(customerLevel.getNextTime());
                customer.setUpdateInfo(SecurityUtils.getUsername());
                this.saveOrUpdate(customer);
            }
        }

        task.setApprovalStatus(approvalStatus);
        task.setApprovalTime(new Date());
        task.setApprovalLeaveMsg(approvalLeaveMsg);
        task.setUpdateInfo(SecurityUtils.getUsername());
        sysTaskService.updateSysTask(task);
    }


    @Override
    public List<CustomerFailureVo> selectFailureList(CustomerFailureVo failureVo) {
        return customerMapper.selectFailureList(failureVo);
    }

    @Override
    public Map<String, Object> selectCustomerFailureById(Long customerId) {
        return customerMapper.selectCustomerFailureById(customerId);
    }

    @Override
    @Transactional
    public void customerActivate(Long[] failureIds,String activateReason) {
        for (Long failureId : failureIds) {
            CustomerFailure customerFailure = customerFailureService.getById(failureId);
            Customer customer = this.getById(customerFailure.getCustomerId());
            customer.setStatus("2");
            customer.setUserId(customerFailure.getOrgUserId());
            customer.setCreateInfo(SecurityUtils.getUsername());
            this.saveOrUpdate(customer);
            customerFailure.setActivateReason(activateReason);
            customerFailure.setDelFlag("1");
            customerFailure.setUpdateInfo(SecurityUtils.getUsername());
            customerFailureService.saveOrUpdate(customerFailure);
        }
    }

    @Override
    @Transactional
    public String importData(List<CustomerExportDto> customerVoList,String importType) {
        if (StringUtils.isNull(customerVoList) || customerVoList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        if (customerVoList.size() >1000) {
            throw new ServiceException("导入最大限制1000条！");
        }
        String importNo = SecurityUtils.getUsername()+ DateUtils.parseDateToStr("YYYYMMDDHHMMSS",new Date())+ RandomUtil.randomNumbers(4);
        if("1".equals(importType)){
            importNo = "p"+importNo;
        } else if ("2".equals(importType)) {
            importNo = "x"+importNo;
        }
        int successNum = 0;//成功条数
        int failureNum = 0;//失败条数
        int repeatNum = 0;//重复条数
        StringBuilder successMsg = new StringBuilder();
        for (CustomerExportDto customerVo : customerVoList) {
            try {
                if(ObjectUtil.isNull(customerVo.getPhonenumber())&&ObjectUtil.isEmpty(customerVo.getPhonenumber())){
                    continue;
                }
                Customer customer = Customer.builder()
                        .name(customerVo.getName())
                        .phonenumber(customerVo.getPhonenumber())
                        .wechat(customerVo.getWechat())
                        .sex(customerVo.getSex())
                        .age(Convert.toLong(customerVo.getAge()))
                        .level(customerVo.getLevel())
                        .nature(customerVo.getNature())
                        .businessType(customerVo.getBusinessType())
                        .stage(customerVo.getStage())
                        .source("PI_LIANG_DAO_RU")
                        .referrer(customerVo.getReferrer())
                        .referrerPhone(customerVo.getReferrerPhone())
                        .referrerWechat(customerVo.getReferrerWechat())
                        .status("0")
                        .importNo(importNo)
                        .importType(importType)
                        .build();
                customer.setCreateInfo(SecurityUtils.getUsername());
                customer.setUpdateInfo(SecurityUtils.getUsername());
                successNum++;
                //判断是否有重复数据
                List<Customer> customers = this.list(new LambdaQueryWrapper<Customer>()
                        .eq(Customer::getDelFlag,0)
                        .eq(Customer::getPhonenumber,customer.getPhonenumber())
                );
                if(customers.size()>0){
                    repeatNum++;
                }
                this.save(customer);

            }catch (Exception e){
                failureNum++;
                log.error("线索导入失败===========>", e);
            }
            //successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
            //failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
            //String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
            //failureMsg.append(msg + e.getMessage());
        }
        successMsg.insert(0, "本次导入线索共 " + successNum + " 条，其中重复条数"+repeatNum+"条");
        return successMsg.toString();
    }

    @Override
    public List<CustomerRepeatVo> selectCustomerRepeatList(Customer customer) {
        List<CustomerRepeatVo> repeatVoList = new ArrayList<>();
        List<CustomerExportVo> exportVoList = customerMapper.selectCustomerRepeatList(customer);
        for (CustomerExportVo customerVo : exportVoList) {
            CustomerRepeatVo repeatVo = new CustomerRepeatVo();
            BeanUtil.copyProperties(customerVo,repeatVo);
            List<CustomerExportVo> customerExportVos = customerMapper.selectCustomerList(Customer.builder().id(Convert.toLong(customerVo.getCustomerId())).phonenumber(customerVo.getPhonenumber()).build());
            repeatVo.setChildren(customerExportVos);
            repeatVoList.add(repeatVo);
        }
        return repeatVoList;
    }

    @Override
    public void repeat(Long [] customerIds) {
        for (Long customerId : customerIds) {
            Customer customer = this.getById(customerId);
            this.remove(new LambdaQueryWrapper<Customer>()
                    .ne(Customer::getId,customerId)
                    .eq(Customer::getPhonenumber,customer.getPhonenumber())
            );
        }
    }

    @Override
    public void repeatDelete(Long[] customerIds) {
        this.removeByIds(Convert.toList(customerIds));
    }

    @Override
    public Map<String, Object> customerInfo(Long id) {
        return customerMapper.customerInfo(id);
    }
}
