package com.tianshu.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysDept;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.utils.DateUtils;
import com.tianshu.common.utils.RandomCodeUtil;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.common.utils.StringUtils;
import com.tianshu.system.domain.vo.SysFilialeAddReqVo;
import com.tianshu.system.domain.vo.SysFilialeReqVo;
import com.tianshu.system.domain.vo.SysFilialeUpdateReqVo;
import com.tianshu.system.mapper.SysDeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.system.mapper.SysFilialeMapper;
import com.tianshu.system.domain.SysFiliale;
import com.tianshu.system.service.ISysFilialeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分公司管理Service业务层处理
 *
 * @author hao
 * @date 2023-01-10
 */
@Service
@Slf4j
@Transactional
public class SysFilialeServiceImpl extends ServiceImpl<SysFilialeMapper, SysFiliale> implements ISysFilialeService {
    @Autowired
    private SysFilialeMapper sysFilialeMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询分公司管理
     *
     * @param id 分公司管理主键
     * @return 分公司管理
     */
    @Override
    public SysFiliale selectSysFilialeById(String filialeId) {
        return sysFilialeMapper.selectSysFilialeById(filialeId);
    }


    /**
     * 查询分公司管理列表
     *
     * @param sysFiliale 分公司管理
     * @return 分公司管理
     */
    @Override
    public List<SysFiliale> selectSysFilialeList(SysFiliale sysFiliale) {
        return sysFilialeMapper.selectSysFilialeList(sysFiliale);
    }

    @Override
    public TableDataInfo querySysFilialeList(SysFilialeReqVo request) {
        log.info("查询分公司列表接口入参：{}", JSON.toJSONString(request));
        LambdaQueryWrapper<SysFiliale> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysFiliale::getDelFlag, "0");
        if (StringUtils.isNotBlank(request.getFilialeName())) {
            queryWrapper.like(SysFiliale::getFilialeName, request.getFilialeName());
        }
        queryWrapper.orderByDesc(SysFiliale::getCreateTime);
        Long total = baseMapper.selectCount(queryWrapper);
        List<SysFiliale> rowsList = new ArrayList<>();
        TableDataInfo tableDataInfo = new TableDataInfo();
        if (total == 0) {
            tableDataInfo.setCode(200);
            tableDataInfo.setMsg("操作成功");
            tableDataInfo.setTotal(total);
            tableDataInfo.setRows(rowsList);
            return tableDataInfo;
        }
        Page<SysFiliale> page = new Page<>();
        page.setCurrent(request.getPageNum());
        page.setSize(request.getPageSize());
        Page<SysFiliale> filialePage = baseMapper.selectPage(page, queryWrapper);
        rowsList = filialePage.getRecords();

        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("操作成功");
        tableDataInfo.setRows(rowsList);
        tableDataInfo.setTotal(total);
        return tableDataInfo;
    }


    @Transactional
    @Override
    public void addSysFilialeList(SysFilialeAddReqVo request) {
        log.info("新增分公司列表接口入参：{}", JSON.toJSONString(request));
        SysFiliale sysFiliale = new SysFiliale();
        sysFiliale.setCompanyName(request.getCompanyName());
        sysFiliale.setFilialeId(RandomCodeUtil.getDataIdLong());
        sysFiliale.setFilialeName(request.getFilialeName());
        sysFiliale.setCityName(request.getCityName());
        sysFiliale.setCityCode(request.getCityCode());
        sysFiliale.setFilialeAddress(request.getFilialeAddress());
        sysFiliale.setGeneralManager(request.getGeneralManager());
        sysFiliale.setFilialePhone(request.getFilialePhone());
        sysFiliale.setFilialeIdentifier(request.getFilialeIdentifier());
        sysFiliale.setFilialeAccountNum(request.getFilialeAccountNum());
        sysFiliale.setFilialeOpenBank(request.getFilialeOpenBank());
        sysFiliale.setCompanyId(request.getCompanyId());
        sysFiliale.setCreateTime(new Date());
        sysFiliale.setCreateBy(SecurityUtils.getUsername());
        sysFiliale.setCityCodeList(JSON.toJSONString(request.getCityCodeList()));
        this.save(sysFiliale);
        addDeptForFiliale(sysFiliale);
    }

    /**
     * @param sysFiliale
     */
    private void addDeptForFiliale(SysFiliale sysFiliale) {

        SysDept sysDeptF = sysDeptMapper.selectDeptByCpyId(sysFiliale.getCompanyId());
        //todo 是否需要判断查询回来的bean非空
        SysDept sysDept = new SysDept();
        sysDept.setParentId(sysDeptF.getDeptId());
        sysDept.setDeptName(sysFiliale.getFilialeName());
        sysDept.setPhone(sysFiliale.getFilialePhone());
        sysDept.setLeader(sysFiliale.getGeneralManager());
        StringBuffer ancestorsBuffer = new StringBuffer();
        ancestorsBuffer = ancestorsBuffer.append("0,").append(sysDeptF.getDeptId());
        sysDept.setAncestors(ancestorsBuffer.toString());
        sysDept.setCreateTime(new Date());
        sysDept.setCreateBy(SecurityUtils.getUsername());
        sysDept.setDeptCompanyId(sysFiliale.getId());
        sysDept.setDelFlag("0");
        sysDept.setStatus("0");
        sysDeptMapper.insertDept(sysDept);
    }

    @Override
    public AjaxResult updateSysFilialeList(SysFilialeUpdateReqVo request) {
        log.info("编辑分公司列表接口入参：{}", JSON.toJSONString(request));
        LambdaUpdateWrapper<SysFiliale> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysFiliale::getCompanyName, request.getCompanyName());
        updateWrapper.set(SysFiliale::getFilialeName, request.getFilialeName());
        updateWrapper.set(SysFiliale::getCityName, request.getCityName());
        updateWrapper.set(SysFiliale::getFilialeAddress, request.getFilialeAddress());
        updateWrapper.set(SysFiliale::getGeneralManager, request.getGeneralManager());
        updateWrapper.set(SysFiliale::getFilialePhone, request.getFilialePhone());
        updateWrapper.set(SysFiliale::getFilialeIdentifier, request.getFilialeIdentifier());
        updateWrapper.set(SysFiliale::getFilialeAccountNum, request.getFilialeAccountNum());
        updateWrapper.set(SysFiliale::getFilialeOpenBank, request.getFilialeOpenBank());
        updateWrapper.set(SysFiliale::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SysFiliale::getCityCodeList,JSON.toJSONString(request.getCityCodeList()));
        //修改人
        updateWrapper.set(SysFiliale::getUpdateTime, new Date());
        updateWrapper.eq(SysFiliale::getFilialeId, request.getFilialeId());
        int i = baseMapper.update(null, updateWrapper);
        if (i == 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteSysFilialeList(Long filialeId) {
        log.info("删除公司信息，操作人{},待删除公司id{}", SecurityUtils.getUsername(), filialeId);
        LambdaUpdateWrapper<SysFiliale> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysFiliale::getDelFlag, "1");
        updateWrapper.set(SysFiliale::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SysFiliale::getUpdateTime, new Date());
        updateWrapper.eq(SysFiliale::getFilialeId, filialeId);
        int filialeDel = baseMapper.update(null, updateWrapper);
        //创建返回值载体
        JSONObject result = new JSONObject();
        if (filialeDel == 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }


    /**
     * 新增分公司管理
     *
     * @param sysFiliale 分公司管理
     * @return 结果
     */
    @Override
    public int insertSysFiliale(SysFiliale sysFiliale) {
        sysFiliale.setCreateTime(DateUtils.getNowDate());
        return sysFilialeMapper.insertSysFiliale(sysFiliale);
    }

    /**
     * 修改分公司管理
     *
     * @param sysFiliale 分公司管理
     * @return 结果
     */
    @Override
    public int updateSysFiliale(SysFiliale sysFiliale) {
        sysFiliale.setUpdateTime(DateUtils.getNowDate());
        return sysFilialeMapper.updateSysFiliale(sysFiliale);
    }

    /**
     * 批量删除分公司管理
     *
     * @param ids 需要删除的分公司管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFilialeByIds(Long[] ids) {
        return sysFilialeMapper.deleteSysFilialeByIds(ids);
    }

    /**
     * 删除分公司管理信息
     *
     * @param id 分公司管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFilialeById(Long id) {
        return sysFilialeMapper.deleteSysFilialeById(id);
    }
}
