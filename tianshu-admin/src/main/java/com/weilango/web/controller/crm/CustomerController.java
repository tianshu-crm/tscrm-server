package com.weilango.web.controller.crm;

import cn.hutool.core.map.MapUtil;
import com.weilango.common.annotation.Log;
import com.weilango.common.core.controller.BaseController;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.common.core.page.TableDataInfo;
import com.weilango.common.core.text.Convert;
import com.weilango.common.enums.BusinessType;
import com.weilango.common.utils.poi.ExcelUtil;
import com.weilango.crm.domain.*;
import com.weilango.crm.domain.dto.CustomerAddDto;
import com.weilango.crm.domain.vo.CustomerExportDto;
import com.weilango.crm.domain.vo.CustomerExportVo;
import com.weilango.crm.domain.vo.CustomerFailureVo;
import com.weilango.crm.domain.vo.CustomerRepeatVo;
import com.weilango.crm.service.ICustomerService;
import com.weilango.crm.service.ICustomerTrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 线索管理Controller
 * 
 * @author hao
 * @date 2023-01-09
 */
@Api(tags = "线索管理管理")
@RestController
@RequestMapping("/crm/customer")
public class CustomerController extends BaseController
{
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerTrackService customerTrackService;

    /**
     * 查询线索管理列表（待分配）
     */
    @ApiOperation("查询线索管理列表")
    @GetMapping("/list")
    public TableDataInfo list(Customer customer)
    {
        startPage();
        customer.setStatus("0");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }

    /**
     * 导出线索管理列表
     */
    @ApiOperation("导出线索管理列表")
    //@PreAuthorize("@ss.hasPermi('crm:customer:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Customer customer)
    {
        customer.setStatus("0");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        ExcelUtil<CustomerExportVo> util = new ExcelUtil<>(CustomerExportVo.class);
        util.exportExcel(response, list, "导出线索");
    }

    /**
     * 获取线索管理详细信息
     */
    @ApiOperation("获取线索管理详细信息")
    //@PreAuthorize("@ss.hasPermi('crm:customer:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") Long customerId)
    {
        return AjaxResult.success(customerService.selectCustomerById(customerId));
    }

    @ApiOperation("线索轨迹详情")
    //@PreAuthorize("@ss.hasPermi('crm:customer:trackInfo')")
    @GetMapping(value = "/trackInfo/{trackId}")
    public AjaxResult getTrackInfo(@PathVariable("trackId") Long trackId)
    {
        CustomerTrack customerTrack = customerTrackService.getById(trackId);
        Map<String, Object> map = customerService.selectCustomerById(customerTrack.getCustomerId());
        map.put("type",customerTrack.getType());
        map.put("callDuration",customerTrack.getCallDuration());
        map.put("content",customerTrack.getContent());
        map.put("nextTime",customerTrack.getNextTime());
        map.put("nextContent",customerTrack.getNextContent());
        return AjaxResult.success(map);
    }


    /**
     * 新增线索管理
     */
    @ApiOperation("新增线索")
    //@PreAuthorize("@ss.hasPermi('crm:customer:add')")
    //@Log(title = "线索管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerAddDto customer)
    {
        customerService.insertCustomer(customer);
        return AjaxResult.success();
    }

    /**
     * 修改线索管理
     */
    @ApiOperation("新增轨迹")
    //@PreAuthorize("@ss.hasPermi('crm:customer:edit')")
    //@Log(title = "线索管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerAddDto customerAddDto)
    {
        customerService.updateCustomer(customerAddDto);
        return AjaxResult.success();
    }

    /**
     * 删除线索管理
     */
    @ApiOperation("删除线索管理")
    //@PreAuthorize("@ss.hasPermi('crm:customer:remove')")
    @Log(title = "线索管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerService.deleteCustomerByIds(ids));
    }


    /**
     * 新增线索管理
     */
    @ApiOperation("批量分配顾问")
    //@PreAuthorize("@ss.hasPermi('crm:customer:allotUser')")
    @PostMapping("/allotUser")
    public AjaxResult allotUser(@RequestBody Map<String,Object> map)
    {
        Long userId = Convert.toLong(map.get("name"));
        String nextTime = Convert.toStr(map.get("nextTime"));
        Convert.toLong(map.get("userId"));
        Long[] customerIds = Convert.toLongArray(map.get("customerIds").toString());
        customerService.allotUser(userId,customerIds,nextTime);
        return AjaxResult.success();
    }

    /**
     * 取消分配
     */
    @ApiOperation("取消分配")
    //@PreAuthorize("@ss.hasPermi('crm:customer:cancel')")
    @PostMapping("/cancel/{customerId}")
    public AjaxResult cancel(@PathVariable Long customerId)
    {
        customerService.cancel(customerId);
        return AjaxResult.success();
    }

    /**
     * 待跟进线索列表
     */
    @ApiOperation("待跟进线索列表")
    @GetMapping("/noFollowList")
    public TableDataInfo noFollowList(Customer customer)
    {
        startPage();
        customer.setStatus("1");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }


    @ApiOperation("待跟进导出")
    //@PreAuthorize("@ss.hasPermi('crm:customer:export')")
    @PostMapping("/noFollowList/export")
    public void noFollowExport(HttpServletResponse response, Customer customer)
    {
        customer.setStatus("1");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        ExcelUtil<CustomerExportVo> util = new ExcelUtil<>(CustomerExportVo.class);
        util.exportExcel(response, list, "导出线索");
    }

    /**
     * 跟进中线索列表
     */
    @ApiOperation("跟进中线索列表")
    @GetMapping("/followingList")
    public TableDataInfo followingList(Customer customer)
    {
        startPage();
        customer.setStatus("2");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }

    @ApiOperation("待跟进导出")
    //@PreAuthorize("@ss.hasPermi('crm:customer:followingExport')")
    @PostMapping("/followingLsit/export")
    public void followingExport(HttpServletResponse response, Customer customer)
    {
        customer.setStatus("2");
        List<CustomerExportVo> list = customerService.selectCustomerList(customer);
        ExcelUtil<CustomerExportVo> util = new ExcelUtil<>(CustomerExportVo.class);
        util.exportExcel(response, list, "导出线索");
    }

    /**
     * 获取线索信息
     */
    @ApiOperation("获取线索信息")
    //@PreAuthorize("@ss.hasPermi('crm:customer:cancel')")
    @GetMapping("/customerInfo/{customerId}")
    public AjaxResult customerInfo(@PathVariable Long customerId)
    {
        Map<Object, Object> map = MapUtil.newHashMap();
        Map<String, Object> resultMap = customerService.customerInfo(customerId);
        map.put("name",resultMap.get("name"));
        map.put("sex",resultMap.get("sex"));
        map.put("age",resultMap.get("age"));
        map.put("level",resultMap.get("level"));
        map.put("userName",resultMap.get("userName"));
        map.put("wechat",resultMap.get("wechat"));
        map.put("phonenumber",resultMap.get("phonenumber"));
        map.put("businessType",resultMap.get("businessType"));
        map.put("nature",resultMap.get("nature"));
        return AjaxResult.success(map);
    }


    /**
     * 批量转移顾问
     */
    @ApiOperation("转移顾问")
    //@PreAuthorize("@ss.hasPermi('crm:customer:allotUser')")
    @PostMapping("/transfer")
    public AjaxResult transfer(@RequestBody CustomerTransfer transfer)
    {
        customerService.customerTransfer(transfer);
        return AjaxResult.success();
    }

    /**
     * 调级
     */
    @ApiOperation("调级")
    //@PreAuthorize("@ss.hasPermi('crm:customer:allotUser')")
    @PostMapping("/levelUpd")
    public AjaxResult levelUpd(@RequestBody CustomerLevel level)
    {
        customerService.customerLevel(level);
        return AjaxResult.success();
    }

    /**
     * 战败
     */
    @ApiOperation("战败")
    //@PreAuthorize("@ss.hasPermi('crm:customer:allotUser')")
    @PostMapping("/failure")
    public AjaxResult failure(@RequestBody CustomerFailure failure)
    {
        customerService.customerFailure(failure);
        return AjaxResult.success();
    }

    /**
     * 审批
     */
    @ApiOperation("审批")
    //@PreAuthorize("@ss.hasPermi('crm:customer:allotUser')")
    @PostMapping("/audit")
    public AjaxResult failure(@RequestBody Map<String,Object> map)
    {
        Long taskId = Convert.toLong(map.get("taskId"));
        String approvalStatus = Convert.toStr(map.get("approvalStatus"));
        String approvalLeaveMsg = Convert.toStr(map.get("approvalLeaveMsg"));
        customerService.customerAudit(taskId,approvalStatus,approvalLeaveMsg);
        return AjaxResult.success();
    }


    /**
     * 查询战败池列表
     */
    @ApiOperation("查询战败池列表")
    @GetMapping("/failureList")
    public TableDataInfo failureList(CustomerFailureVo failureVo)
    {
        startPage();
        List<CustomerFailureVo> list = customerService.selectFailureList(failureVo);
        return getDataTable(list);
    }

    /**
     * 导出线索管理列表
     */
    @ApiOperation("导出战败池列表")
    //@PreAuthorize("@ss.hasPermi('crm:customer:failureExport')")
    @PostMapping("/failureExport")
    public void failureExport(HttpServletResponse response, CustomerFailureVo failureVo)
    {
        List<CustomerFailureVo> list = customerService.selectFailureList(failureVo);
        ExcelUtil<CustomerFailureVo> util = new ExcelUtil<>(CustomerFailureVo.class);
        util.exportExcel(response, list, "导出战败池");
    }

    /**
     * 获取线索管理详细信息
     */
    @ApiOperation("获取战败详情")
    //@PreAuthorize("@ss.hasPermi('crm:customer:failureQuery')")
    @GetMapping(value = "/failure/{customerId}")
    public AjaxResult failure(@PathVariable("customerId") Long customerId)
    {
        return AjaxResult.success(customerService.selectCustomerFailureById(customerId));
    }

    /**
     * 激活
     */
    @ApiOperation("激活")
    //@PreAuthorize("@ss.hasPermi('crm:customer:activate')")
    @PostMapping(value = "/activate")
    public AjaxResult activate(@RequestBody Map<String,Object> map)
    {
        Long [] failureIds = Convert.toLongArray(Convert.toStr(map.get("failureId")));
        String activateReason = Convert.toStr(map.get("activateReason"));
        customerService.customerActivate(failureIds,activateReason);
        return AjaxResult.success();
    }

    /**
     * 激活
     */
    @ApiOperation("激活线索")
    //@PreAuthorize("@ss.hasPermi('crm:customer:activateAll')")
    @PostMapping(value = "/activateAll")
    public AjaxResult activateAll(@RequestBody Map<String,Object> map)
    {
        Long[] failureIds = Convert.toLongArray(",",Convert.toStr(map.get("failureIds")));
        String activateReason = Convert.toStr(map.get("activateReason"));
        customerService.customerActivate(failureIds,activateReason);
        return AjaxResult.success();
    }


    @ApiOperation("下载导入模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<CustomerExportDto> util = new ExcelUtil<>(CustomerExportDto.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 导入数据
     * @param file
     * @return
     * @throws Exception
     */
    //@PreAuthorize("@ss.hasPermi('crm:customer:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file,String importType) throws Exception
    {
        ExcelUtil<CustomerExportDto> util = new ExcelUtil<>(CustomerExportDto.class);
        List<CustomerExportDto> customerVoList = util.importExcel(file.getInputStream());
        String message = customerService.importData(customerVoList,importType);
        return success(message);
    }

    @ApiOperation("去重线索管理列表")
    @GetMapping("/repeatList")
    public TableDataInfo repeatList(Customer customer)
    {
        startPage();
        List<CustomerRepeatVo> list = customerService.selectCustomerRepeatList(customer);
        return getDataTable(list);
    }

    @ApiOperation("替换数据")
    //@PreAuthorize("@ss.hasPermi('crm:customer:repeat')")
    @PutMapping("/repeat/{customerIds}")
    public AjaxResult repeat(@PathVariable Long[] customerIds){
        customerService.repeat(customerIds);
        return AjaxResult.success();
    }

    @ApiOperation("删除数据")
    //@PreAuthorize("@ss.hasPermi('crm:customer:repeatDelete')")
    @DeleteMapping("/repeatDelete/{customerIds}")
    public AjaxResult repeatDelete(@PathVariable Long[] customerIds){
        customerService.repeatDelete(customerIds);
        return AjaxResult.success();
    }

}
