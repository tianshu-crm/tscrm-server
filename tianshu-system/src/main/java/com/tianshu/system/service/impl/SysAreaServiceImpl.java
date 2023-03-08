package com.tianshu.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysUser;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.common.core.text.Convert;
import com.tianshu.common.utils.DateUtils;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.common.utils.StringUtils;
import com.tianshu.system.domain.po.CityCodeTbl;
import com.tianshu.system.domain.po.SysArea;
import com.tianshu.system.domain.po.SysAreaRangeLog;
import com.tianshu.system.domain.vo.*;
import com.tianshu.system.mapper.SysAreaMapper;
import com.tianshu.system.mapper.SysAreaRangeLogMapper;
import com.tianshu.system.mapper.SysCityTblMapper;
import com.tianshu.system.mapper.SysUserMapper;
import com.tianshu.system.service.ISysAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hao
 * @date 2023-01-10
 */
@Service
@Slf4j
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService
{
    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysAreaRangeLogMapper sysAreaRangeLogMapper;

    @Autowired
    private SysCityTblMapper sysCityTblMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public SysArea selectSysAreaById(Long id)
    {
        return sysAreaMapper.selectSysAreaById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysArea 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysArea> selectSysAreaList(SysArea sysArea)
    {
        return sysAreaMapper.selectSysAreaList(sysArea);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysArea(SysArea sysArea)
    {
        sysArea.setCreateTime(DateUtils.getNowDate());
        return sysAreaMapper.insertSysArea(sysArea);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysArea(SysArea sysArea)
    {
        sysArea.setUpdateTime(DateUtils.getNowDate());
        return sysAreaMapper.updateSysArea(sysArea);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByIds(Long[] ids)
    {
        return sysAreaMapper.deleteSysAreaByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaById(Long id)
    {
        return sysAreaMapper.deleteSysAreaById(id);
    }

    @Override
    public List<QueryAreaNameListRespVo> queryAreaNameList() {
        //只查没有被删除有效对
        LambdaQueryWrapper<SysArea> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysArea::getDelFlag,"0");

        List<SysArea> sysAreas = baseMapper.selectList(queryWrapper);
        List<QueryAreaNameListRespVo> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sysAreas)){
            return result;
        }

        sysAreas.stream().forEach(area ->{
            QueryAreaNameListRespVo queryAreaNameListRespVo = QueryAreaNameListRespVo.builder()
                    .dictLabel(area.getAreaName()).dictValue(String.valueOf(area.getId())).build();
            result.add(queryAreaNameListRespVo);
        });
        return result;
    }

    @Override
    public Object queryAreaList(QueryAreaListReqVo request) {
        log.info("查询区域列表接口入参:{}", JSON.toJSONString(request));
        //开始构建数据查询对象
        LambdaQueryWrapper<SysArea> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysArea::getDelFlag,"0");
        if (StringUtils.isNotBlank(request.getAreaId())){
            queryWrapper.eq(SysArea::getId,request.getAreaId());
        }
        //模糊查询区域范围
        if (StringUtils.isNotBlank(request.getCityName())){
            queryWrapper.like(SysArea::getAreaRange,request.getCityName());
        }
        Long total = baseMapper.selectCount(queryWrapper);
        List<SysArea> rows = new ArrayList<>();
        //创建结果返回值容器
        TableDataInfo result = new TableDataInfo();
        if (total==0){
            result.setCode(200);
            result.setMsg("操作成功");
            result.setRows(rows);
            result.setTotal(total);
            return result;
        }
        Page<SysArea> page = new Page<>();
        page.setCurrent(request.getPageNum());
        page.setSize(request.getPageSize());

        Page<SysArea> sysAreaPage = baseMapper.selectPage(page, queryWrapper);
        List<SysArea> records = sysAreaPage.getRecords();
        List<QueryAreaListRespVo> queryAreaListRespVos = buildAreaList(records);
        result.setCode(200);
        result.setMsg("操作成功");
        result.setRows(queryAreaListRespVos);
        result.setTotal(total);
        return result;
    }

    /**
     * 创建区域返回列表
     * @param sysArea
     * @return
     */

    private List<QueryAreaListRespVo> buildAreaList(List<SysArea> sysArea){

        List<QueryAreaListRespVo> result = new ArrayList<>();
        sysArea.stream().forEach(area->{
            //先获取待查询的城市code
            JSONArray jsonArray = JSONArray.parseArray(area.getAreaRange().toString());
            //准备本次区域的城市code集合容器
            List<Integer> cityCodeList = new ArrayList<>();

            //获取当前的城市code
            for (int index = 0;index<jsonArray.size();index++){
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                if (jsonObject.getInteger("cityCode")!=null){
                    cityCodeList.add(jsonObject.getInteger("cityCode"));
                }
            }
            JSONObject jsonObject = buildRangeDatas(jsonArray);
            //根据拿到的城市code集合获取当前区域下的员工数量
            LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysUser::getCityCode,cityCodeList);
            Long count = sysUserMapper.selectCount(queryWrapper);
            //构建返回
            QueryAreaListRespVo queryAreaListRespVo = QueryAreaListRespVo.builder()
                    .id(area.getId())
                    .areaId(area.getId()+"")
                    .areaName(area.getAreaName())
                    .areaRange(jsonObject)
                    .staffCount(count)
                    .delFlag(area.getDelFlag()).build();
            result.add(queryAreaListRespVo);
        });
        return result;
    }

    /**
     *
     * @param jsonArray
     * @return
     */
    private JSONObject buildRangeDatas(JSONArray jsonArray) {
        StringBuffer areaRangeStr = new StringBuffer();
        JSONArray areaRangeShow = new JSONArray();
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if (jsonObject.getInteger("cityCode") != null &&
                    StringUtils.isNotBlank(jsonObject.getString("cityName"))) {
                LambdaQueryWrapper<CityCodeTbl> queryWrapper = new LambdaQueryWrapper<>();
                String cityCode = jsonObject.getInteger("cityCode").toString();
                String pCode = cityCode.substring(0, 3)+"000";
                queryWrapper.eq(CityCodeTbl::getRegionCode,pCode);
                CityCodeTbl cityCodeTbl = sysCityTblMapper.selectOne(queryWrapper);
                if (cityCodeTbl==null){
                    continue;
                }
                JSONArray member = new JSONArray();
                member.add(cityCodeTbl.getRegionCode());
                member.add(Integer.parseInt(cityCode));
                areaRangeShow.add(member);
                if (index!=jsonArray.size()-1){
                    areaRangeStr = areaRangeStr.append(cityCodeTbl.getRegionName()+"/"+jsonObject.getString("cityName")+",");
                    continue;
                }
                areaRangeStr = areaRangeStr.append(cityCodeTbl.getRegionName()+"/"+jsonObject.getString("cityName"));
            }
        }
        JSONObject result = new JSONObject();
        result.put("areaRangeStr",areaRangeStr);
        result.put("areaRangeShow",areaRangeShow);
        return result;
    }
    @Transactional
    @Override
    public AjaxResult addArea(AddAreaReqVo request) {
        log.info("区域添加接口入参:{}",JSON.toJSONString(request));
        //先检查区域是否已存在
        JSONObject isExitsResult = checkRange(request);
        if (isExitsResult!=null){
            return AjaxResult.error("区域已经存在",isExitsResult.get("data"));
        }
        //构建入库po
        SysArea sysArea = new SysArea();
        sysArea.setAreaName(request.getAreaName());
        sysArea.setAreaRange(JSON.toJSON(request.getAreaRange()).toString());
        sysArea.setCreateBy(SecurityUtils.getUsername());
        sysArea.setCreateTime(new Date());
        sysArea.setDelFlag("0");
        int insert = baseMapper.insert(sysArea);
        if (insert==0){
            return AjaxResult.error();
        }
        sysArea.setAreaRange(JSON.toJSON(request.getAreaRange()));
        insertRangeLog(sysArea);
        return AjaxResult.success();
    }

    /**
     *对区域表进行添加
     * @param sysArea
     */
    private void insertRangeLog(SysArea sysArea){
        //创建待添加数组
        List<SysAreaRangeLog> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) JSON.toJSON(sysArea.getAreaRange());
        for (int index = 0;index<jsonArray.size();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            SysAreaRangeLog sysAreaRangeLog = SysAreaRangeLog.builder()
                    .cityCode(jsonObject.getInteger("cityCode"))
                    .cityName(jsonObject.getString("cityName"))
                    .areaId(sysArea.getId()+"")
                    .areaName(sysArea.getAreaName())
                    .build();
            list.add(sysAreaRangeLog);
        }
        sysAreaRangeLogMapper.insertAreaLog(list);
    }

    /**
     * 检查区域是否已经被添加过
     * @param request
     * @return 结果
     */
    private JSONObject checkRange(AddAreaReqVo request){
        //获取
        Object areaRange = request.getAreaRange();
        JSONArray jsonArray = (JSONArray)JSON.toJSON(areaRange);
        List<Integer> cityCodeList = new ArrayList<>();
        //获取本次添加的code
        for (int index = 0;index<jsonArray.size();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            cityCodeList.add(jsonObject.getInteger("cityCode"));
        }
        //查询是否有被添加过
        LambdaQueryWrapper<SysAreaRangeLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysAreaRangeLog::getCityCode, cityCodeList);
        List<SysAreaRangeLog> sysAreaRangeLogs = sysAreaRangeLogMapper.selectList(queryWrapper);
        //根据查询是否有被查询回来做判断是否可以添加
        if (CollectionUtils.isEmpty(sysAreaRangeLogs)){
            return null;
        }
        JSONObject result = new JSONObject();
        List<String> isExitCity = sysAreaRangeLogs.stream().map(item -> item.getCityName()).collect(Collectors.toList());
        result.put("code",500);
        result.put("msg","部分区域已被添加");
        result.put("data",isExitCity);
        return result;
    }

    @Override
    public AjaxResult updateArea(UpdateAreaReqVo request) {
        log.info("修改区域接口入参:{}",JSON.toJSONString(request));
        //修改po
        LambdaUpdateWrapper<SysArea> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysArea::getAreaName,request.getAreaName());
        updateWrapper.set(SysArea::getAreaRange,JSON.toJSONString(request.getAreaRange()));
        updateWrapper.eq(SysArea::getId,request.getAreaId());
        int update = baseMapper.update(null, updateWrapper);
        if (update==0){
            return AjaxResult.error();
        }
        //先删除原有的区域id下的辖区
        sysAreaRangeLogMapper.deleteAreaLog(request.getAreaId());
        //在进行添加
        SysArea sysArea = new SysArea();
        sysArea.setId(Convert.toLong(request.getAreaId()));
        sysArea.setAreaRange(request.getAreaRange());
        insertRangeLog(sysArea);

        return AjaxResult.success();
    }

    @Transactional
    @Override
    public AjaxResult deleteArea(String areaId) {
        log.info("删除区域接口入参:{}",areaId);
        LambdaUpdateWrapper<SysArea> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysArea::getDelFlag,"1");
        updateWrapper.eq(SysArea::getId,areaId);
        int update = baseMapper.update(null, updateWrapper);
        if (update==0){
            return AjaxResult.error();
        }
        sysAreaRangeLogMapper.deleteAreaLog(areaId);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult checkAreaNameExist(CheckAreaNameIsExist request) {
        //创建返回值
        JSONObject result = new JSONObject();
        //获取区域id，若传递了区域id则说明是编辑接口
        if (StringUtils.isNotBlank(request.getAreaId())){
            LambdaQueryWrapper<SysArea> queryForUpdate = Wrappers.lambdaQuery();
            queryForUpdate.eq(SysArea::getId,request.getAreaId());
            SysArea sysArea = baseMapper.selectOne(queryForUpdate);
            if (sysArea == null){
                return AjaxResult.error("区域不存在");
            }
            //若当前区域名称与查询出来的数据中的区域名称一样则返回可以使用
            if ((request.getAreaName().trim()).equals(sysArea.getAreaName())){
                return AjaxResult.success("可以使用");
            }
        }

        //检查当前的区域名称是否已经存在
        LambdaQueryWrapper<SysArea> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysArea::getAreaName,request.getAreaName());
        List<SysArea> sysAreas = baseMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(sysAreas)){
            return AjaxResult.success("可以使用");
        }
        return AjaxResult.error("此区域名称已存在");
    }

    @Override
    public QueryAreaListRespVo selectForUpdate(String areaId) {
        LambdaQueryWrapper<SysArea> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysArea::getId,areaId);
        SysArea sysArea = baseMapper.selectOne(queryWrapper);
        if (sysArea==null){
            return null;
        }
        Object areaRange = sysArea.getAreaRange();
        if(areaRange==null){
            return null;
        }
        JSONArray jsonArray = JSONArray.parseArray(sysArea.getAreaRange().toString());

        JSONObject jsonObject = buildRangeDatas(jsonArray);
        QueryAreaListRespVo result = QueryAreaListRespVo.builder()
                .id(sysArea.getId())
                .areaId(sysArea.getId()+"")
                .areaName(sysArea.getAreaName())
                .areaRange(jsonObject).build();

        return result;
    }
}
