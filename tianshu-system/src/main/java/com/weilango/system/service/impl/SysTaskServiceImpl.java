package com.weilango.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.core.domain.entity.SysDept;
import com.weilango.common.core.domain.entity.SysDictData;
import com.weilango.common.core.domain.entity.SysUser;
import com.weilango.common.core.page.TableDataInfo;
import com.weilango.common.core.text.Convert;
import com.weilango.common.utils.DateUtils;
import com.weilango.common.utils.SecurityUtils;
import com.weilango.system.domain.po.SysTask;
import com.weilango.system.domain.vo.AddTaskReqVo;
import com.weilango.system.domain.vo.QueryTaskDetailResVo;
import com.weilango.system.domain.vo.QueryTaskListReqVo;
import com.weilango.system.domain.vo.QueryTaskListRespVo;
import com.weilango.system.mapper.SysDictDataMapper;
import com.weilango.system.mapper.SysTaskMapper;
import com.weilango.system.service.ISysDeptService;
import com.weilango.system.service.ISysTaskService;
import com.weilango.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service业务层处理
 * 
 * @author hao
 * @date 2023-01-13
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements ISysTaskService
{
    @Autowired
    private SysTaskMapper sysTaskMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    private SimpleDateFormat sdfymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SimpleDateFormat sdfymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询
     * 
     * @param id 主键
     * @return 
     */
    @Override
    public SysTask selectSysTaskById(Long id)
    {
        return sysTaskMapper.selectSysTaskById(id);
    }

    /**
     * 查询列表
     * 
     * @param sysTask 
     * @return 
     */
    @Override
    public List<SysTask> selectSysTaskList(SysTask sysTask)
    {
        return sysTaskMapper.selectSysTaskList(sysTask);
    }

    /**
     * 新增
     * 
     * @param sysTask 
     * @return 结果
     */
    @Override
    public int insertSysTask(SysTask sysTask)
    {
        sysTask.setCreateTime(DateUtils.getNowDate());
        return sysTaskMapper.insertSysTask(sysTask);
    }

    /**
     * 修改
     * 
     * @param sysTask 
     * @return 结果
     */
    @Override
    public int updateSysTask(SysTask sysTask)
    {
        sysTask.setUpdateTime(DateUtils.getNowDate());
        return sysTaskMapper.updateSysTask(sysTask);
    }

    /**
     * 批量删除
     * 
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteSysTaskByIds(Long[] ids)
    {
        return sysTaskMapper.deleteSysTaskByIds(ids);
    }

    /**
     * 删除信息
     * 
     * @param id 主键
     * @return 结果
     */
    @Override
    public int deleteSysTaskById(Long id)
    {
        return sysTaskMapper.deleteSysTaskById(id);
    }

    @Override
    public TableDataInfo queryTaskList(QueryTaskListReqVo queryTaskListReqVo) {
        //创建查询对象
        LambdaQueryWrapper<SysTask> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysTask::getProcessorId,queryTaskListReqVo.getUserId());
        queryWrapper.eq(SysTask::getType,queryTaskListReqVo.getTaskType());
        //todo 因为没有删除操作，所以删除暂时不作为删除标记
        //查询数量
        Long total = baseMapper.selectCount(queryWrapper);
        List<QueryTaskListRespVo> resultList = new ArrayList<>();
        //创建返回值容器
        TableDataInfo result = new TableDataInfo();
        if (total==0){
            result.setCode(200);
            result.setMsg("查询成功");
            result.setRows(resultList);
            result.setTotal(total);
            return result;
        }
        queryWrapper.orderByDesc(SysTask::getCreateBy);
        //创建分页对象
        Page<SysTask> page = new Page<>();
        page.setCurrent(queryTaskListReqVo.getPageNum());
        page.setSize(queryTaskListReqVo.getPageSize());
        Page<SysTask> resultPage = baseMapper.selectPage(page, queryWrapper);
        result.setCode(200);
        result.setMsg("查询成功");
        result.setRows(buildTaskRespVo(resultPage.getRecords()));
        result.setTotal(total);
        return result;
    }

    @Override
    public QueryTaskDetailResVo queryTaskBydetail(Long id) {
        LambdaQueryWrapper<SysTask> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysTask::getId,id);
        SysTask sysTask = baseMapper.selectOne(queryWrapper);
        if (sysTask==null){
            return null;
        }
        //前端要求不调用数据词典，让后端自己弄好给传过去
        LambdaQueryWrapper<SysDictData> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysDictData::getDictType,"crm_task_business_type");
        lambdaQueryWrapper.eq(SysDictData::getDictValue,sysTask.getBusinessType());
        SysDictData sysDictData = sysDictDataMapper.selectOne(lambdaQueryWrapper);

        return QueryTaskDetailResVo.builder().id(sysTask.getId().toString())
                .taskBusinessType(sysTask.getBusinessType())
                .sponsorUserName(sysTask.getSponsorUserName())
                .affiliatedUnit(sysTask.getAffiliatedUnit())
                .sponsorTime(sdfymdhms.format(sysTask.getSponsorTime()))
                .title(sysTask.getTitle())
                .approvalStatus(sysTask.getApprovalStatus())
                .taskDetail(JSONObject.parseObject(sysTask.getDetail())).build();
    }


    /**
     * 构建响应vo用
     * @param dataBase
     * @return
     */
    private List<QueryTaskListRespVo> buildTaskRespVo(List<SysTask> dataBase){

        //前端要求不调用数据词典，让后端自己弄好给传过去
        LambdaQueryWrapper<SysDictData> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysDictData::getDictType,"crm_task_business_type");
        List<SysDictData> sysDictDataList = sysDictDataMapper.selectList(lambdaQueryWrapper);
        Map<String,String> dictMap = new HashMap<>();
        sysDictDataList.stream().forEach(sysDictData -> {
            dictMap.put(sysDictData.getDictValue(),sysDictData.getDictLabel());
        });

        List<QueryTaskListRespVo> result = new ArrayList<>();
        dataBase.stream().forEach(data->{
            QueryTaskListRespVo queryTaskListRespVo = QueryTaskListRespVo.builder()
                    .id(data.getId())
                    .title(data.getTitle())
                    .businessType(dictMap.get(data.getBusinessType()))
                    .createTime(sdfymdhm.format(data.getCreateTime())).build();
            result.add(queryTaskListRespVo);
        });
        return result;
    }

    @Override
    public int addTaskReqVo(AddTaskReqVo addTaskReqVo) {
        //开始创建入库po
        SysTask sysTask = new SysTask();
        sysTask.setProcessorId(addTaskReqVo.getProcessorId());
        sysTask.setTitle(addTaskReqVo.getTitle());
        sysTask.setBusinessType(addTaskReqVo.getBusinessType());
        sysTask.setSponsorUserId(addTaskReqVo.getSponsorUserId());
        sysTask.setSponsorUserName(addTaskReqVo.getSponsorUserName());
        sysTask.setAffiliatedUnit(addTaskReqVo.getAffiliatedUnit());
        sysTask.setSponsorTime(addTaskReqVo.getSponsorTime());
        sysTask.setDetail(JSON.toJSONString(addTaskReqVo.getDetail()));
        sysTask.setCreateInfo(SecurityUtils.getUsername());
        sysTask.setApprovalStatus(addTaskReqVo.getApprovalStatus());
        sysTask.setType("1");
        return baseMapper.insert(sysTask);

    }

    @Override
    @Transactional
    public void sendTask(SysTask sysTask) {
        sysTask.setSponsorUserId(SecurityUtils.getUserId());
        sysTask.setSponsorUserName(SecurityUtils.getUsername());
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        if(ObjectUtil.isNotNull(sysUser.getDeptId())){
            SysDept sysDept = sysDeptService.selectDeptById(sysUser.getDeptId());
            /*if (ObjectUtil.isEmpty(sysDept.getLeader())){
                return;
            }
            //获取任务处理人是否是当前审批人
            if(sysTask.getSponsorUserId().equals(sysDept.getLeader())){
                return;
            }*/
            sysTask.setAffiliatedUnit(sysDept.getDeptName());
            sysTask.setProcessorId(Convert.toLong(sysDept.getDeptId()));
            sysTask.setApprovalStatus("2");
            sysTask.setSponsorTime(new Date());
            sysTask.setCreateInfo(SecurityUtils.getUsername());
            this.save(sysTask);
        }
    }
}
