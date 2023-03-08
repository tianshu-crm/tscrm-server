package com.tianshu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.system.domain.po.CityCodeTbl;

public interface SysCityCodeService extends IService<CityCodeTbl> {

    Object getProvinceList();

    Object getCityList(String cityCode);

    Object getCityInfo();

}
