package com.weilango.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.weilango.common.utils.poi.ExcelUtil;
import com.weilango.system.domain.po.SysTask;
import com.weilango.system.domain.vo.AddTaskReqVo;
import com.weilango.system.domain.vo.QueryTaskDetailResVo;
import com.weilango.system.domain.vo.QueryTaskListReqVo;
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
import com.weilango.common.annotation.Log;
import com.weilango.common.core.controller.BaseController;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.common.enums.BusinessType;
import com.weilango.system.service.ISysTaskService;
import com.weilango.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * Controller
 * 
 * @author hao
 * @date 2023-01-13
 */
@Api(tags = "管理")
@RestController
@RequestMapping("/system/task")
public class SysTaskController extends BaseController
{
    @Autowired
    private ISysTaskService sysTaskService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @PreAuthorize("@ss.hasPermi('system:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTask sysTask)
    {
        startPage();
        List<SysTask> list = sysTaskService.selectSysTaskList(sysTask);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @ApiOperation("导出列表")
    @PreAuthorize("@ss.hasPermi('system:task:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTask sysTask)
    {
        List<SysTask> list = sysTaskService.selectSysTaskList(sysTask);
        ExcelUtil<SysTask> util = new ExcelUtil<SysTask>(SysTask.class);
        util.exportExcel(response, list, "数据");
    }

    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysTaskService.selectSysTaskById(id));
    }

    /**
     * 新增
     */
    @ApiOperation("新增")
    @PreAuthorize("@ss.hasPermi('system:task:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTask sysTask)
    {
        return toAjax(sysTaskService.insertSysTask(sysTask));
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
//    @PreAuthorize("@ss.hasPermi('system:task:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTask sysTask)
    {
        return toAjax(sysTaskService.updateSysTask(sysTask));
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PreAuthorize("@ss.hasPermi('system:task:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysTaskService.deleteSysTaskByIds(ids));
    }

    /**
     * 获取任务列表
     * @param queryTaskListVo
     * @return
     */
    @PostMapping("/tasklist")
    public TableDataInfo getTaskList(@RequestBody QueryTaskListReqVo queryTaskListVo){
        return sysTaskService.queryTaskList(queryTaskListVo);
    }

    /**
     * 获取任务详情
     * @param id
     * @return
     */
    @GetMapping("/taskdetail/{id}")
    public AjaxResult getTaskDetail(@PathVariable("id")Long id){
        QueryTaskDetailResVo queryTaskDetailResVo = sysTaskService.queryTaskBydetail(id);
        if (queryTaskDetailResVo==null){
            return error();
        }
        return success(queryTaskDetailResVo);
    }

    /**
     * 添加任务
     * @param addTaskReqVo
     * @return 结果
     */
    @PostMapping("/addtask")
    public AjaxResult addTaskDetail(@RequestBody AddTaskReqVo addTaskReqVo){

        int i = sysTaskService.addTaskReqVo(addTaskReqVo);
        if (i==0){
            return error();
        }
        return success();

    }

}
