package com.weilango.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.SysFiliale;
import org.apache.ibatis.annotations.Param;

/**
 * 分公司管理Mapper接口
 *
 * @author hao
 * @date 2023-01-10
 */
public interface SysFilialeMapper extends BaseMapper<SysFiliale>
{
    /**
     * 查询分公司管理
     *
     * @param id 分公司管理主键
     * @return 分公司管理
     */
    public SysFiliale selectSysFilialeById(String id);

    /**
     * 查询分公司管理列表
     *
     * @param sysFiliale 分公司管理
     * @return 分公司管理集合
     */
    public List<SysFiliale> selectSysFilialeList(SysFiliale sysFiliale);

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
     * 删除分公司管理
     *
     * @param id 分公司管理主键
     * @return 结果
     */
    public int deleteSysFilialeById(Long id);

    /**
     * 批量删除分公司管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFilialeByIds(Long[] ids);

    /**
     * 通过公司id删除
     * @param companyId
     * @return 结果
     */
    public int deleteSysFilialeByCompanyId(@Param("companyId") String companyId);

    public SysFiliale selectSysFilialeByDbiId(@Param("id") Long id);
}
