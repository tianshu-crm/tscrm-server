package com.tianshu.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.system.domain.SysCompany;
import org.springframework.data.repository.query.Param;

/**
 * 公司管理Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-05
 */
public interface SysCompanyMapper extends BaseMapper<SysCompany>
{
    /**
     * 查询公司管理
     *
     * @param companyId 公司管理主键
     * @return 公司管理
     */
    public SysCompany selectSysCompanyById(@Param("companyId") String companyId);

    /**
     * 查询公司管理列表
     *
     * @param sysCompany 公司管理
     * @return 公司管理集合
     */
    public List<SysCompany> selectSysCompanyList(SysCompany sysCompany);

    /**
     * 新增公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    public int insertSysCompany(SysCompany sysCompany);

    /**
     * 修改公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    public int updateSysCompany(SysCompany sysCompany);

    /**
     * 删除公司管理
     *
     * @param id 公司管理主键
     * @return 结果
     */
    public int deleteSysCompanyById(Long id);

    /**
     * 批量删除公司管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCompanyByIds(Long[] ids);

    public List<SysCompany> selectAll();
}
