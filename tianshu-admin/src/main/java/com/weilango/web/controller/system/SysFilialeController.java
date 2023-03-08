package com.weilango.web.controller.system;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.weilango.common.core.domain.entity.SysMenu;
import com.weilango.system.domain.SysCompany;
import com.weilango.system.domain.vo.SysFilialeAddReqVo;
import com.weilango.system.domain.vo.SysFilialeReqVo;
import com.weilango.system.domain.vo.SysFilialeUpdateReqVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.weilango.common.annotation.Log;
import com.weilango.common.core.controller.BaseController;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.common.enums.BusinessType;
import com.weilango.system.domain.SysFiliale;
import com.weilango.system.service.ISysFilialeService;
import com.weilango.common.utils.poi.ExcelUtil;
import com.weilango.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 分公司管理Controller
 *
 * @author hao
 * @date 2023-01-10
 */
@Api(tags = "分公司管理管理")
@RestController
@RequestMapping("/system/filiale")
public class SysFilialeController extends BaseController
{
    @Autowired
    private ISysFilialeService sysFilialeService;

    /**
     * 查询分公司管理列表
     */
    @ApiOperation("查询分公司管理列表")
    @PreAuthorize("@ss.hasPermi('system:filiale:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFiliale sysFiliale)
    {
        startPage();
        List<SysFiliale> list = sysFilialeService.selectSysFilialeList(sysFiliale);
        return getDataTable(list);
    }

    @ApiOperation("查询分公司管理列表-bymyelf")
    @PostMapping("/query/filialelist")
    public TableDataInfo querySysFilialeList(@Validated @RequestBody SysFilialeReqVo request){
        return sysFilialeService.querySysFilialeList(request);
    }
    @ApiOperation("新增分公司管理列表-bymyelf")
    @PostMapping("/add/filialelist")
    public AjaxResult addSysFilialeList(@Validated @RequestBody SysFilialeAddReqVo request){
        sysFilialeService.addSysFilialeList(request);
        return AjaxResult.success();
    }
    @ApiOperation("编辑分公司管理列表-bymyelf")
    @PostMapping("/update/filialelist")
    public AjaxResult updateSysFilialeList(@Validated @RequestBody SysFilialeUpdateReqVo request){
        return sysFilialeService.updateSysFilialeList(request);
    }
    @ApiOperation("删除分公司管理列表-bymyself")
    @PostMapping("/delete/{filialeid}")
    public AjaxResult deleteSysFiliale(@PathVariable("filialeid") Long filialeId ){
        return sysFilialeService.deleteSysFilialeList(filialeId);
    }


    /**
     * 导出分公司管理列表
     */
    @ApiOperation("导出分公司管理列表")
    @PreAuthorize("@ss.hasPermi('system:filiale:export')")
    @Log(title = "分公司管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFiliale sysFiliale)
    {
        List<SysFiliale> list = sysFilialeService.selectSysFilialeList(sysFiliale);
        ExcelUtil<SysFiliale> util = new ExcelUtil<SysFiliale>(SysFiliale.class);
        util.exportExcel(response, list, "分公司管理数据");
    }

    /**
     * 获取分公司管理详细信息
     */
    @ApiOperation("获取分公司管理详细信息")
    //@PreAuthorize("@ss.hasPermi('system:filiale:query')")
    @GetMapping(value = "/{filialeId}")
    public AjaxResult getInfo(@PathVariable("filialeId") String filialeId)
    {
        HashMap<Object, Object> map = MapUtil.newHashMap();
        SysFiliale sysFiliale = sysFilialeService.selectSysFilialeById(filialeId);
        map.put("companyName",sysFiliale.getCompanyName());
        map.put("filialeName",sysFiliale.getFilialeName());
        map.put("cityName",sysFiliale.getCityName());
        map.put("cityCode",sysFiliale.getCityCode());
        map.put("filialeAddress",sysFiliale.getFilialeAddress());
        map.put("generalManager",sysFiliale.getGeneralManager());
        map.put("filialePhone",sysFiliale.getFilialePhone());
        map.put("filialeIdentifier",sysFiliale.getFilialeIdentifier());
        map.put("filialeAccountNum",sysFiliale.getFilialeAccountNum());
        map.put("filialeOpenBank",sysFiliale.getFilialeOpenBank());
        map.put("cityCodeList", JSON.toJSONString(sysFiliale.getCityCodeList()));
        return AjaxResult.success(map);
    }

    /**
     * 新增分公司管理
     */
    @ApiOperation("新增分公司管理")
    @PreAuthorize("@ss.hasPermi('system:filiale:add')")
    @Log(title = "分公司管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFiliale sysFiliale)
    {
        return toAjax(sysFilialeService.insertSysFiliale(sysFiliale));
    }

    /**
     * 修改分公司管理
     */
    @ApiOperation("修改分公司管理")
    @PreAuthorize("@ss.hasPermi('system:filiale:edit')")
    @Log(title = "分公司管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFiliale sysFiliale)
    {
        return toAjax(sysFilialeService.updateSysFiliale(sysFiliale));
    }

    /**
     * 删除分公司管理
     */
    @ApiOperation("删除分公司管理")
    @PreAuthorize("@ss.hasPermi('system:filiale:remove')")
    @Log(title = "分公司管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysFilialeService.deleteSysFilialeByIds(ids));
    }
}
