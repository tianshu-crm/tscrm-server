package com.weilango.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilango.system.domain.po.SysAreaRangeLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAreaRangeLogMapper extends BaseMapper<SysAreaRangeLog> {

    public int insertAreaLog(List<SysAreaRangeLog> list);

    public int deleteAreaLog(@Param("areaId") String areaId);
}
