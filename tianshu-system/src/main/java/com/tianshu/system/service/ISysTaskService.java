package com.tianshu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.system.domain.po.SysTask;
import com.tianshu.system.domain.vo.AddTaskReqVo;
import com.tianshu.system.domain.vo.QueryTaskDetailResVo;
import com.tianshu.system.domain.vo.QueryTaskListReqVo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hao
 * @date 2023-01-13
 */
public interface ISysTaskService extends IService<SysTask>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysTask selectSysTaskById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysTask 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysTask> selectSysTaskList(SysTask sysTask);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysTask 【请填写功能名称】
     * @return 结果
     */
    public int insertSysTask(SysTask sysTask);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysTask 【请填写功能名称】
     * @return 结果
     */
    public int updateSysTask(SysTask sysTask);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteSysTaskByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysTaskById(Long id);

    public TableDataInfo queryTaskList(QueryTaskListReqVo queryTaskListReqVo);

    public QueryTaskDetailResVo queryTaskBydetail(Long id);

    public int addTaskReqVo(AddTaskReqVo addTaskReqVo);

    /**
     * 发送审批任务
     * @param sysTask
     * @return
     */
    public void sendTask(SysTask sysTask);
}
