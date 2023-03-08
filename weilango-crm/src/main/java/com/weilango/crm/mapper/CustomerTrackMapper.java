package com.weilango.crm.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.crm.domain.CustomerTrack;

/**
 * 线索轨迹Mapper接口
 * 
 * @author hao
 * @date 2023-01-09
 */
public interface CustomerTrackMapper extends BaseMapper<CustomerTrack>
{
    /**
     * 查询线索轨迹
     * 
     * @param id 线索轨迹主键
     * @return 线索轨迹
     */
    public CustomerTrack selectCustomerTrackById(Long id);

    /**
     * 查询线索轨迹列表
     * 
     * @param customerTrack 线索轨迹
     * @return 线索轨迹集合
     */
    public List<Map<String,Object>> selectCustomerTrackList(CustomerTrack customerTrack);

    /**
     * 新增线索轨迹
     * 
     * @param customerTrack 线索轨迹
     * @return 结果
     */
    public int insertCustomerTrack(CustomerTrack customerTrack);

    /**
     * 修改线索轨迹
     * 
     * @param customerTrack 线索轨迹
     * @return 结果
     */
    public int updateCustomerTrack(CustomerTrack customerTrack);

    /**
     * 删除线索轨迹
     * 
     * @param id 线索轨迹主键
     * @return 结果
     */
    public int deleteCustomerTrackById(Long id);

    /**
     * 批量删除线索轨迹
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerTrackByIds(Long[] ids);
}
