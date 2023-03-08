package com.tianshu.web.controller.crm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianshu.common.core.controller.BaseController;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.utils.DateUtils;
import com.tianshu.common.utils.poi.ExcelUtil;
import com.tianshu.crm.domain.Customer;
import com.tianshu.crm.domain.CustomerDetail;
import com.tianshu.crm.domain.CustomerTrialrun;
import com.tianshu.crm.domain.vo.CustomerExportVo;
import com.tianshu.crm.domain.vo.CustomerTrialrunVo;
import com.tianshu.crm.service.ICustomerDetailService;
import com.tianshu.crm.service.ICustomerService;
import com.tianshu.crm.service.ICustomerTrialrunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 试乘试驾Controller
 * 
 * @author hao
 * @date 2023-01-11
 */
@Api(tags = "试乘试驾管理")
@RestController
@RequestMapping("/crm/customerTrialrun")
public class CustomerTrialrunController extends BaseController
{
    @Autowired
    private ICustomerTrialrunService customerTrialrunService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDetailService customerDetailService;

    /**
     * 查询试乘试驾列表
     */
    @ApiOperation("查询试乘试驾列表")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerTrialrun customerTrialrun)
    {
        startPage();
        List<CustomerTrialrunVo> list = customerTrialrunService.selectCustomerTrialrunList(customerTrialrun);
        return getDataTable(list);
    }

    /**
     * 导出试乘试驾列表
     */
    @ApiOperation("导出试乘试驾列表")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:export')")
    //@Log(title = "试乘试驾", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerTrialrun customerTrialrun)
    {
        List<CustomerTrialrunVo> list = customerTrialrunService.selectCustomerTrialrunList(customerTrialrun);
        ExcelUtil<CustomerTrialrunVo> util = new ExcelUtil<CustomerTrialrunVo>(CustomerTrialrunVo.class);
        util.exportExcel(response, list, "试乘试驾数据");
    }

    /**
     * 获取试乘试驾详细信息
     */
    @ApiOperation("获取试乘试驾详细信息")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:query')")
    @GetMapping(value = "/{trialrunId}")
    public AjaxResult getInfo(@PathVariable("trialrunId") Long trialrunId)
    {
        HashMap<String, Object> map = MapUtil.newHashMap();
        CustomerTrialrun customerTrialrun = customerTrialrunService.selectCustomerTrialrunById(trialrunId);
        map.put("id",customerTrialrun.getId());
        map.put("name",customerTrialrun.getName());
        map.put("phonenumber",customerTrialrun.getPhonenumber());
        map.put("type",customerTrialrun.getType());
        map.put("status",customerTrialrun.getStatus());
        map.put("source",customerTrialrun.getSource());
        map.put("storeName",customerTrialrun.getStoreName());
        map.put("storeCode",customerTrialrun.getStoreCode());
        map.put("businessType",customerTrialrun.getBusinessType());
        map.put("sex",customerTrialrun.getSex());
        map.put("trialrunTime", DateUtil.format(customerTrialrun.getTrialrunTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        map.put("trialrunName",customerTrialrun.getTrialrunName());
        map.put("trialrunPhone",customerTrialrun.getTrialrunPhone());
        map.put("trialrunCarType",customerTrialrun.getTrialrunCarType());
        map.put("trialrunCarSeries",customerTrialrun.getTrialrunCarSeries());
        map.put("trialrunNo",customerTrialrun.getTrialrunNo());
        map.put("userId",ObjectUtil.isNotNull(customerTrialrun.getUserId())? customerTrialrun.getUserId()+"":"");
        map.put("remark",customerTrialrun.getRemark());

        Map<Object, Object> customerMap = MapUtil.newHashMap();
        if(ObjectUtil.isNotNull(customerTrialrun.getCustomerId())){
            List<CustomerExportVo> customerList = customerService.selectCustomerList(Customer.builder().customerId(customerTrialrun.getCustomerId()).build());
            CustomerExportVo customer = customerList.get(0);
            CustomerDetail customerDetail = customerDetailService.getOne(new LambdaQueryWrapper<CustomerDetail>()
                    .eq(CustomerDetail::getCustomerId,customer.getCustomerId())
            );
            customerMap.put("customerId",customer.getCustomerId());
            customerMap.put("name",customer.getName());
            customerMap.put("phonenumber",customer.getPhonenumber());
            customerMap.put("source",customer.getSource());
            customerMap.put("businessType",customer.getBusinessType());
            customerMap.put("status",customer.getStatus());
            customerMap.put("createTime",customer.getCreateTime());
            customerMap.put("userName",customer.getUserName());
            customerMap.put("intentionCarType",customerDetail.getIntentionCarType());
            customerMap.put("intentionCarSeries",customerDetail.getIntentionCarSeries());
        }
        map.put("customerInfo",customerMap);
        return AjaxResult.success(map);
    }

    /**
     * 新增试乘试驾
     */
    @ApiOperation("新增试乘试驾")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:add')")
    @PostMapping
    public AjaxResult add(@RequestBody CustomerTrialrun customerTrialrun)
    {
        customerTrialrunService.insertCustomerTrialrun(customerTrialrun);
        return AjaxResult.success();
    }

    /**
     * 修改试乘试驾
     */
    @ApiOperation("修改试乘试驾")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerTrialrun customerTrialrun)
    {
        return toAjax(customerTrialrunService.updateCustomerTrialrun(customerTrialrun));
    }

    /**
     * 删除试乘试驾
     */
    @ApiOperation("删除试乘试驾")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrialrun:remove')")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerTrialrunService.deleteCustomerTrialrunByIds(ids));
    }
}
