package com.tianshu.web.controller.crm;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tianshu.common.annotation.Log;
import com.tianshu.common.core.controller.BaseController;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.enums.BusinessType;
import com.tianshu.crm.domain.CustomerDetail;
import com.tianshu.crm.service.ICustomerDetailService;
import com.tianshu.common.utils.poi.ExcelUtil;
import com.tianshu.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 线索详情Controller
 * 
 * @author hao
 * @date 2023-01-09
 */
@Api(tags = "线索详情管理")
@RestController
@RequestMapping("/crm/customerDetail")
public class CustomerDetailController extends BaseController
{
    @Autowired
    private ICustomerDetailService customerDetailService;

    /**
     * 查询线索详情列表
     */
    @ApiOperation("查询线索详情列表")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerDetail customerDetail)
    {
        startPage();
        List<CustomerDetail> list = customerDetailService.selectCustomerDetailList(customerDetail);
        return getDataTable(list);
    }

    /**
     * 导出线索详情列表
     */
    @ApiOperation("导出线索详情列表")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:export')")
    @Log(title = "线索详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerDetail customerDetail)
    {
        List<CustomerDetail> list = customerDetailService.selectCustomerDetailList(customerDetail);
        ExcelUtil<CustomerDetail> util = new ExcelUtil<>(CustomerDetail.class);
        util.exportExcel(response, list, "线索详情数据");
    }

    /**
     * 获取线索详情详细信息
     */
    @ApiOperation("获取线索详情详细信息")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(customerDetailService.selectCustomerDetailById(id));
    }

    /**
     * 新增线索详情
     */
    @ApiOperation("新增线索详情")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:add')")
    @Log(title = "线索详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerDetail customerDetail)
    {
        return toAjax(customerDetailService.insertCustomerDetail(customerDetail));
    }

    /**
     * 修改线索详情
     */
    @ApiOperation("修改线索详情")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:edit')")
    @Log(title = "线索详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerDetail customerDetail)
    {
        return toAjax(customerDetailService.updateCustomerDetail(customerDetail));
    }

    /**
     * 删除线索详情
     */
    @ApiOperation("删除线索详情")
    @PreAuthorize("@ss.hasPermi('crm:customerDetail:remove')")
    @Log(title = "线索详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerDetailService.deleteCustomerDetailByIds(ids));
    }
}
