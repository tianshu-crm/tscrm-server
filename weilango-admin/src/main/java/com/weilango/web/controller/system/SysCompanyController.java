package com.weilango.web.controller.system;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.weilango.common.annotation.Log;
import com.weilango.common.core.controller.BaseController;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.common.core.page.TableDataInfo;
import com.weilango.common.enums.BusinessType;
import com.weilango.common.utils.poi.ExcelUtil;
import com.weilango.system.domain.SysCompany;
import com.weilango.system.domain.vo.SysCompanyAddReqVo;
import com.weilango.system.domain.vo.SysCompanyReqVo;
import com.weilango.system.domain.vo.SysCompanyUpdateReqVo;
import com.weilango.system.service.SysCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
/**
 * 公司管理Controller
 *
 * @author ruoyi
 * @date 2023-01-05
 */
@Api(tags = "公司管理管理")
@RestController
@RequestMapping("/system/company")
public class SysCompanyController extends BaseController
{
    @Autowired
    private SysCompanyService sysCompanyService;

    /**
     * 查询公司管理列表
     */
    @ApiOperation("查询公司管理列表")
    @PreAuthorize("@ss.hasPermi('system:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(@Validated @RequestBody SysCompanyReqVo request)
    {
        List<SysCompany> list = sysCompanyService.selectSysCompanyList(request);

        return getDataTable(list);
    }

    @ApiOperation("查询公司管理列表(非框架所有)")
    @PostMapping("/query/companylist")
    public TableDataInfo queryCompanyList(@Validated @RequestBody SysCompanyReqVo request){
        return sysCompanyService.querySysCompanyList(request);

    }

    /**
     * 导出公司管理列表
     */
    @ApiOperation("导出公司管理列表")
    @PreAuthorize("@ss.hasPermi('system:company:export')")
    @Log(title = "公司管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCompanyReqVo request)
    {
        List<SysCompany> list = sysCompanyService.selectSysCompanyList(request);
        ExcelUtil<SysCompany> util = new ExcelUtil<SysCompany>(SysCompany.class);
        util.exportExcel(response, list, "公司管理数据");
    }

    /**
     * 获取公司管理详细信息
     */
    @ApiOperation("获取公司管理详细信息")
    //@PreAuthorize("@ss.hasPermi('system:company:query')")
    @GetMapping(value = "/{companyId}")
    public AjaxResult getInfo(@PathVariable("companyId") String companyId)
    {
        HashMap<Object, Object> map = MapUtil.newHashMap();
        SysCompany sysCompany = sysCompanyService.selectSysCompanyById(companyId);
        map.put("companyName",sysCompany.getCompanyName());
        map.put("companyRegAddress",sysCompany.getCompanyRegAddress());
        map.put("legalPersonName",sysCompany.getLegalPersonName());
        map.put("companyPhone",sysCompany.getCompanyPhone());
        map.put("cityName",sysCompany.getCityName());
        map.put("cityCode",sysCompany.getCityCode());
        map.put("identifier",sysCompany.getIdentifier());
        map.put("accountNum",sysCompany.getAccountNum());
        map.put("accountOpenBank",sysCompany.getAccountOpenBank());
        map.put("companyRegTime",sysCompany.getCompanyRegTime());
        map.put("cityCodeList",JSON.toJSONString(sysCompany.getCityCodeList()));
        return AjaxResult.success(map);
    }

    /**
     * 新增公司管理
     */
    @ApiOperation("新增公司管理")
    @PreAuthorize("@ss.hasPermi('system:company:add')")
    @Log(title = "公司管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysCompany sysCompany)
    {
        return toAjax(sysCompanyService.insertSysCompany(sysCompany));
    }

    /**
     * 修改公司管理
     */
    @ApiOperation("修改公司管理")
    @PreAuthorize("@ss.hasPermi('system:company:edit')")
    @Log(title = "公司管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody SysCompany sysCompany)
    {
        return toAjax(sysCompanyService.updateSysCompany(sysCompany));
    }

    /**
     * 公司管理 新增公司 自己写的
     * @param request
     * @return
     */
    @ApiOperation("公司管理 新增公司 自己写的")
    @PostMapping("/add/companyinfo")
    public AjaxResult addSysCompany(@Validated @RequestBody SysCompanyAddReqVo request){
         return sysCompanyService.addSysCompany(request);
    }

    /**
     * 公司管理 修改公司 自己写的
     * @param request
     * @return
     */
    @ApiOperation("公司管理 修改公司 自己写的")
    @PostMapping("/update/companyinfo")
    public AjaxResult updateSysCompany(@Validated @RequestBody SysCompanyUpdateReqVo request){
        return sysCompanyService.updateCompany(request);
    }

    /**
     * 公司管理 删除公司 连携子公司一同删除
     * @param companyId
     * @return
     */
    @ApiOperation("公司管理 删除公司 连携子公司一同删除")
    @PostMapping("/delete/{companyid}")
    public AjaxResult deleteSysCompany(@PathVariable("companyid") Long companyId ){
        return sysCompanyService.deleteCompany(companyId);
    }

    /**
     * 删除公司管理
     */
    @ApiOperation("删除公司管理")
    @PreAuthorize("@ss.hasPermi('system:company:remove')")
    @Log(title = "公司管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCompanyService.deleteSysCompanyByIds(ids));
    }

    /**
     * 获取公司的公司code与公司名称
     * @return
     */
    //todo 暂时不做修改，需要前端配合
    @GetMapping("/company/list")
    public Object getCompanyList(){
        return sysCompanyService.getCompanyList();
    }
}
