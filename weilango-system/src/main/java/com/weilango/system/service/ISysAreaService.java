package com.weilango.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.system.domain.po.SysArea;
import com.weilango.system.domain.vo.*;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface ISysAreaService extends IService<SysArea>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysArea selectSysAreaById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysArea 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysArea 【请填写功能名称】
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteSysAreaByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysAreaById(Long id);

    /**
     * 查询区域下拉列表
     * @return 结果
     */
    public List<QueryAreaNameListRespVo> queryAreaNameList();

    /**
     * 查询区域列表
     * @return
     */
    public Object queryAreaList(QueryAreaListReqVo request);

    /**
     * 添加区域
     * @param request
     * @return 结果
     */
    public AjaxResult addArea(AddAreaReqVo request);

    /**
     * 修改区域
     * @param request
     * @return 结果
     */
    public AjaxResult updateArea(UpdateAreaReqVo request);

    /**
     * 用户id
     * @param areaId
     * @return 结果
     */
    public AjaxResult deleteArea(String areaId);

    /**
     * 检查区域名称是否已经存在
     * @param request
     * @return
     */
    public AjaxResult checkAreaNameExist(CheckAreaNameIsExist request);

    /**
     * 区域id
     * @param areaId
     * @return
     */
    public QueryAreaListRespVo selectForUpdate(String areaId);
}
