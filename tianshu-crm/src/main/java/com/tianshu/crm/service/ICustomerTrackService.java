package com.tianshu.crm.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.crm.domain.CustomerTrack;

/**
 * 线索轨迹Service接口
 * 
 * @author hao
 * @date 2023-01-09
 */
public interface ICustomerTrackService extends IService<CustomerTrack>
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
     * 批量删除线索轨迹
     * 
     * @param ids 需要删除的线索轨迹主键集合
     * @return 结果
     */
    public int deleteCustomerTrackByIds(Long[] ids);

    /**
     * 删除线索轨迹信息
     * 
     * @param id 线索轨迹主键
     * @return 结果
     */
    public int deleteCustomerTrackById(Long id);
}
