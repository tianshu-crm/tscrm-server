package com.weilango.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilango.system.domain.po.CityCodeTbl;

public interface SysCityCodeService extends IService<CityCodeTbl> {

    Object getProvinceList();

    Object getCityList(String cityCode);

    Object getCityInfo();

}
