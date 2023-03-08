package com.tianshu.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.system.domain.SysFiliale;
import com.tianshu.system.domain.vo.SysFilialeAddReqVo;
import com.tianshu.system.domain.vo.SysFilialeReqVo;
import com.tianshu.system.domain.vo.SysFilialeUpdateReqVo;

/**
 * 分公司管理Service接口
 *
 * @author hao
 * @date 2023-01-10
 */
public interface ISysFilialeService extends IService<SysFiliale>
{
    /**
     * 查询分公司管理
     *
     * @param id 分公司管理主键
     * @return 分公司管理
     */
    public SysFiliale selectSysFilialeById(String filialeId);




    /**
     * 查询分公司管理列表
     *
     * @param sysFiliale 分公司管理
     * @return 分公司管理集合
     */
    public List<SysFiliale> selectSysFilialeList(SysFiliale sysFiliale);

    /**
     * 查询分公司管理 - by-myself
     * @param request
     * @return
     */

    TableDataInfo querySysFilialeList(SysFilialeReqVo request);

    /**
     * 新增分公司管理 - by-myself
     * @param request
     * @return
     */
    void addSysFilialeList(SysFilialeAddReqVo request);
    /**
     * 编辑分公司管理 - by-myself
     */
    AjaxResult updateSysFilialeList(SysFilialeUpdateReqVo request);
    /**
     * 删除分公司管理 - by-myself
     */
    AjaxResult deleteSysFilialeList(Long filialeId);

    /**
     * 新增分公司管理
     *
     * @param sysFiliale 分公司管理
     * @return 结果
     */
    public int insertSysFiliale(SysFiliale sysFiliale);

    /**
     * 修改分公司管理
     *
     * @param sysFiliale 分公司管理
     * @return 结果
     */
    public int updateSysFiliale(SysFiliale sysFiliale);

    /**
     * 批量删除分公司管理
     *
     * @param ids 需要删除的分公司管理主键集合
     * @return 结果
     */
    public int deleteSysFilialeByIds(Long[] ids);

    /**
     * 删除分公司管理信息
     *
     * @param id 分公司管理主键
     * @return 结果
     */
    public int deleteSysFilialeById(Long id);
}
