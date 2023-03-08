package com.weilango.crm.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.crm.mapper.CustomerTrackMapper;
import com.weilango.crm.domain.CustomerTrack;
import com.weilango.crm.service.ICustomerTrackService;

/**
 * 线索轨迹Service业务层处理
 * 
 * @author hao
 * @date 2023-01-09
 */
@Service
public class CustomerTrackServiceImpl extends ServiceImpl<CustomerTrackMapper, CustomerTrack> implements ICustomerTrackService
{
    @Autowired
    private CustomerTrackMapper customerTrackMapper;

    /**
     * 查询线索轨迹
     * 
     * @param id 线索轨迹主键
     * @return 线索轨迹
     */
    @Override
    public CustomerTrack selectCustomerTrackById(Long id)
    {
        return customerTrackMapper.selectCustomerTrackById(id);
    }

    /**
     * 查询线索轨迹列表
     * 
     * @param customerTrack 线索轨迹
     * @return 线索轨迹
     */
    @Override
    public List<Map<String,Object>> selectCustomerTrackList(CustomerTrack customerTrack)
    {
        return customerTrackMapper.selectCustomerTrackList(customerTrack);
    }

    /**
     * 新增线索轨迹
     * 
     * @param customerTrack 线索轨迹
     * @return 结果
     */
    @Override
    public int insertCustomerTrack(CustomerTrack customerTrack)
    {
        customerTrack.setCreateTime(DateUtils.getNowDate());
        return customerTrackMapper.insertCustomerTrack(customerTrack);
    }

    /**
     * 修改线索轨迹
     * 
     * @param customerTrack 线索轨迹
     * @return 结果
     */
    @Override
    public int updateCustomerTrack(CustomerTrack customerTrack)
    {
        customerTrack.setUpdateTime(DateUtils.getNowDate());
        return customerTrackMapper.updateCustomerTrack(customerTrack);
    }

    /**
     * 批量删除线索轨迹
     * 
     * @param ids 需要删除的线索轨迹主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTrackByIds(Long[] ids)
    {
        return customerTrackMapper.deleteCustomerTrackByIds(ids);
    }

    /**
     * 删除线索轨迹信息
     * 
     * @param id 线索轨迹主键
     * @return 结果
     */
    @Override
    public int deleteCustomerTrackById(Long id)
    {
        return customerTrackMapper.deleteCustomerTrackById(id);
    }
}
