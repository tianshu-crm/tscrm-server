package com.weilango.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.weilango.system.domain.po.SysArea;
import com.weilango.system.domain.vo.*;
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
import com.weilango.system.service.ISysAreaService;
import com.weilango.common.utils.poi.ExcelUtil;
import com.weilango.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 【请填写功能名称】Controller
 * 
 * @author hao
 * @date 2023-01-10
 */
@Api(tags = "【请填写功能名称】管理")
@RestController
@RequestMapping("/system/area")
public class SysAreaController extends BaseController
{
    @Autowired
    private ISysAreaService sysAreaService;

    /**
     * 查询【请填写功能名称】列表
     */
    @ApiOperation("查询【请填写功能名称】列表")
    @PreAuthorize("@ss.hasPermi('system:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysArea sysArea)
    {
        startPage();
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @ApiOperation("导出【请填写功能名称】列表")
    @PreAuthorize("@ss.hasPermi('system:area:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysArea sysArea)
    {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        ExcelUtil<SysArea> util = new ExcelUtil<SysArea>(SysArea.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @ApiOperation("获取【请填写功能名称】详细信息")
    @PreAuthorize("@ss.hasPermi('system:area:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysAreaService.selectSysAreaById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @ApiOperation("新增【请填写功能名称】")
    @PreAuthorize("@ss.hasPermi('system:area:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysArea sysArea)
    {
        return toAjax(sysAreaService.insertSysArea(sysArea));
    }

    /**
     * 修改【请填写功能名称】
     */
    @ApiOperation("修改【请填写功能名称】")
    @PreAuthorize("@ss.hasPermi('system:area:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysArea sysArea)
    {
        return toAjax(sysAreaService.updateSysArea(sysArea));
    }

    /**
     * 删除【请填写功能名称】
     */
    @ApiOperation("删除【请填写功能名称】")
    @PreAuthorize("@ss.hasPermi('system:area:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysAreaService.deleteSysAreaByIds(ids));
    }


    /**
     * 获取区域名称
     * @return 结果
     */
    @GetMapping("/areaname/list")
    public AjaxResult queryAreaList(){
        return success(sysAreaService.queryAreaNameList());
    }

    /**
     * 获取区域列表
     * 返回值不改(涉及到了多表联查)
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/area/list")
    public Object queryAreaList(@Validated @RequestBody QueryAreaListReqVo request){
        return sysAreaService.queryAreaList(request);
    }

    /**
     * 添加区域
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/add/area")
    public AjaxResult addAreaInfo(@Validated @RequestBody AddAreaReqVo request){
        return sysAreaService.addArea(request);
    }

    /**
     * 修改区域
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/update/area")
    public AjaxResult updateAreaInfo(@Validated @RequestBody UpdateAreaReqVo request){
        return sysAreaService.updateArea(request);
    }

    /**
     * 通过区域id删除区域
     * @param areaId 区域id
     * @return 结果
     */
    @DeleteMapping("/delete/{areaid}")
    public AjaxResult deleteAreaInfo(@PathVariable("areaid")String areaId){
        return sysAreaService.deleteArea(areaId);
    }

    /**
     * 检查区域是否已经存在
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/check/isexist")
    public AjaxResult selectAreaNameIsExits(@RequestBody CheckAreaNameIsExist request){
        return sysAreaService.checkAreaNameExist(request);
    }

    @GetMapping("/forupdate/{areaid}")
    public AjaxResult getOneAreaInfoForUpdate(@PathVariable("areaid") String areaId){
        QueryAreaListRespVo queryAreaListRespVo = sysAreaService.selectForUpdate(areaId);
        if (queryAreaListRespVo==null){
            return error();
        }
        return success(queryAreaListRespVo);
    }
}
