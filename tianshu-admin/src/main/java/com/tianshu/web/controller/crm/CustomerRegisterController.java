package com.tianshu.web.controller.crm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianshu.common.core.controller.BaseController;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.utils.poi.ExcelUtil;
import com.tianshu.crm.domain.Customer;
import com.tianshu.crm.domain.CustomerDetail;
import com.tianshu.crm.domain.CustomerRegister;
import com.tianshu.crm.domain.vo.CustomerExportVo;
import com.tianshu.crm.domain.vo.CustomerRegisterVo;
import com.tianshu.crm.service.ICustomerDetailService;
import com.tianshu.crm.service.ICustomerRegisterService;
import com.tianshu.crm.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 来店登记Controller
 * 
 * @author hao
 * @date 2023-01-11
 */
@Api(tags = "来店登记管理")
@RestController
@RequestMapping("/crm/customerRegister")
public class CustomerRegisterController extends BaseController
{
    @Autowired
    private ICustomerRegisterService customerRegisterService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDetailService customerDetailService;

    /**
     * 查询来店登记列表
     */
    @ApiOperation("查询来店登记列表")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerRegister customerRegister)
    {
        startPage();
        List<CustomerRegisterVo> list = customerRegisterService.selectCustomerRegisterList(customerRegister);
        return getDataTable(list);
    }

    /**
     * 导出来店登记列表
     */
    @ApiOperation("导出来店登记列表")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:export')")
    //@Log(title = "来店登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerRegister customerRegister)
    {
        List<CustomerRegisterVo> list = customerRegisterService.selectCustomerRegisterList(customerRegister);
        ExcelUtil<CustomerRegisterVo> util = new ExcelUtil<>(CustomerRegisterVo.class);
        util.exportExcel(response, list, "来店登记数据");
    }

    /**
     * 获取来店登记详细信息
     */
    @ApiOperation("获取来店登记详细信息")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:query')")
    @GetMapping(value = "/{registerId}")
    public AjaxResult getInfo(@PathVariable("registerId") Long registerId)
    {
        HashMap<Object, Object> map = MapUtil.newHashMap();
        CustomerRegister register = customerRegisterService.selectCustomerRegisterById(registerId);
        map.put("id",register.getId());
        map.put("name",register.getName());
        map.put("phonenumber",register.getPhonenumber());
        map.put("wechat",register.getWechat());
        map.put("times",register.getTimes());
        map.put("type",register.getType());
        map.put("purpose",register.getPurpose());
        map.put("intentionCarSeries",register.getIntentionCarSeries());
        map.put("intentionCarType",register.getIntentionCarType());
        map.put("referrer",register.getReferrer());
        map.put("referrerPhone",register.getReferrerPhone());
        map.put("referrerWechat",register.getReferrerWechat());
        map.put("userId",ObjectUtil.isNotNull(register.getUserId())? register.getUserId()+"":"");
        map.put("duration",register.getDuration());
        map.put("number",register.getNumber());
        map.put("startTime",DateUtil.format(register.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        map.put("endTime",DateUtil.format(register.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        map.put("isIntroduce",register.getIsIntroduce());
        map.put("isManagerParticipation",register.getIsManagerParticipation());
        map.put("source",register.getSource());
        map.put("carType",register.getCarType());
        map.put("remark",register.getRemark());
        map.put("carNo",register.getCarNo());

        Map<Object, Object> customerMap = MapUtil.newHashMap();
        if(ObjectUtil.isNotNull(register.getCustomerId())){
            List<CustomerExportVo> customerList = customerService.selectCustomerList(Customer.builder().customerId(register.getCustomerId()).build());
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
     * 新增来店登记
     */
    @ApiOperation("新增来店登记")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:add')")
    //@Log(title = "来店登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerRegister customerRegister)
    {
        customerRegisterService.insertCustomerRegister(customerRegister);
        return AjaxResult.success();
    }

    /**
     * 修改来店登记
     */
    @ApiOperation("修改来店登记")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:edit')")
    //@Log(title = "来店登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerRegister customerRegister)
    {
        return toAjax(customerRegisterService.updateCustomerRegister(customerRegister));
    }

    /**
     * 删除来店登记
     */
    @ApiOperation("删除来店登记")
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:remove')")
    //@Log(title = "来店登记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerRegisterService.deleteCustomerRegisterByIds(ids));
    }

    @ApiOperation("下载导入模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<CustomerRegisterVo> util = new ExcelUtil<>(CustomerRegisterVo.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 导入数据
     * @param file
     * @return
     * @throws Exception
     */
    //@PreAuthorize("@ss.hasPermi('crm:customerRegister:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<CustomerRegisterVo> util = new ExcelUtil<>(CustomerRegisterVo.class);
        List<CustomerRegisterVo> registerVoList = util.importExcel(file.getInputStream());
        String message = customerRegisterService.importData(registerVoList);
        return success(message);
    }

    @GetMapping(value = "/customerByPhone/{phonenumber}")
    public AjaxResult customerByPhone(@PathVariable("phonenumber") String phonenumber)
    {
        List<CustomerExportVo> customerList = customerService.selectCustomerList(Customer.builder().phonenumber(phonenumber).build());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (CustomerExportVo customer : customerList) {
            CustomerDetail customerDetail = customerDetailService.getOne(new LambdaQueryWrapper<CustomerDetail>()
                    .eq(CustomerDetail::getCustomerId,customer.getCustomerId())
            );
            Map<String, Object> map = MapUtil.newHashMap();
            map.put("customerId",customer.getCustomerId());
            map.put("name",customer.getName());
            map.put("phonenumber",customer.getPhonenumber());
            map.put("source",customer.getSource());
            map.put("businessType",customer.getBusinessType());
            map.put("status",customer.getStatus());
            map.put("createTime",customer.getCreateTime());
            map.put("userName",customer.getUserName());
            map.put("intentionCarType",customerDetail.getIntentionCarType());
            map.put("intentionCarSeries",customerDetail.getIntentionCarSeries());
            listMap.add(map);
        }
        return AjaxResult.success(listMap);
    }
}
