package com.tianshu.crm.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianshu.crm.domain.CustomerTransfer;

/**
 * 线索转移Mapper接口
 * 
 * @author hao
 * @date 2023-01-10
 */
public interface CustomerTransferMapper extends BaseMapper<CustomerTransfer>
{
    /**
     * 查询线索转移
     * 
     * @param id 线索转移主键
     * @return 线索转移
     */
    public CustomerTransfer selectCustomerTransferById(Long id);

    /**
     * 查询线索转移列表
     * 
     * @param customerTransfer 线索转移
     * @return 线索转移集合
     */
    public List<CustomerTransfer> selectCustomerTransferList(CustomerTransfer customerTransfer);

    /**
     * 新增线索转移
     * 
     * @param customerTransfer 线索转移
     * @return 结果
     */
    public int insertCustomerTransfer(CustomerTransfer customerTransfer);

    /**
     * 修改线索转移
     * 
     * @param customerTransfer 线索转移
     * @return 结果
     */
    public int updateCustomerTransfer(CustomerTransfer customerTransfer);

    /**
     * 删除线索转移
     * 
     * @param id 线索转移主键
     * @return 结果
     */
    public int deleteCustomerTransferById(Long id);

    /**
     * 批量删除线索转移
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerTransferByIds(Long[] ids);
}
