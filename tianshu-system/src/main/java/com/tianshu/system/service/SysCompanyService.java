package com.tianshu.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.system.domain.SysCompany;
import com.tianshu.system.domain.vo.SysCompanyAddReqVo;
import com.tianshu.system.domain.vo.SysCompanyReqVo;
import com.tianshu.system.domain.vo.SysCompanyUpdateReqVo;

/**
 * 公司管理Service接口
 *
 * @author ruoyi
 * @date 2023-01-05
 */
public interface SysCompanyService extends IService<SysCompany>
{
    /**
     * 查询公司管理
     *
     * @param id 公司管理主键
     * @return 公司管理
     */
    public SysCompany selectSysCompanyById(String company);

    /**
     * 查询公司管理列表
     *
     * @param request 公司管理
     * @return 公司管理集合
     */
    public List<SysCompany> selectSysCompanyList(SysCompanyReqVo request);

    /**
     * 公司列表查询
     * @param request 公司列表查询请求参数
     * @return 接口返回值
     */
    TableDataInfo querySysCompanyList(SysCompanyReqVo request);

    /**
     * 新增公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    public int insertSysCompany(SysCompany sysCompany);

    /**
     * 新增公司-自己写的
     * @param request 新增公司
     * @return
     */
    AjaxResult addSysCompany(SysCompanyAddReqVo request);

    /**
     * 公司信息修改-自己写的
     * @param request 修改公司
     * @return
     */
    AjaxResult updateCompany(SysCompanyUpdateReqVo request);

    /**
     * 公司删除-自己写的
     * @param companyId
     * @return
     */
    AjaxResult deleteCompany(Long companyId);

    /**
     * 修改公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    public int updateSysCompany(SysCompany sysCompany);

    /**
     * 批量删除公司管理
     *
     * @param ids 需要删除的公司管理主键集合
     * @return 结果
     */
    public int deleteSysCompanyByIds(Long[] ids);

    /**
     * 删除公司管理信息
     *
     * @param id 公司管理主键
     * @return 结果
     */
    public int deleteSysCompanyById(Long id);

    public Object getCompanyList();
}
