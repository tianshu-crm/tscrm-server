package com.tianshu.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.core.redis.RedisCache;
import com.tianshu.system.domain.po.CityCodeTbl;
import com.tianshu.system.domain.vo.CityCodeInfoRespVo;
import com.tianshu.system.mapper.SysCityTblMapper;
import com.tianshu.system.service.SysCityCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysCityCodeServiceImpl extends ServiceImpl<SysCityTblMapper, CityCodeTbl> implements SysCityCodeService {

    @Autowired
    private RedisCache redisCache;

    private static final String CITY_INFO_ALL_INDEX = "allCityInfoIndex";

    @Override
    public Object getProvinceList() {
        LambdaQueryWrapper<CityCodeTbl> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CityCodeTbl::getRegionLevel,1);
        List<CityCodeTbl> provinceList = baseMapper.selectList(queryWrapper);
        JSONObject resultObj = new JSONObject();
        resultObj.put("code",200);
        resultObj.put("msg","查询成功");
        resultObj.put("rows",provinceList);
        return resultObj;
    }

    @Override
    public Object getCityList(String cityCode) {
        LambdaQueryWrapper<CityCodeTbl> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CityCodeTbl::getParentRegionCode,cityCode);
        List<CityCodeTbl> cityList = baseMapper.selectList(queryWrapper);
        JSONObject resultObj = new JSONObject();
        resultObj.put("code",200);
        resultObj.put("msg","查询成功");
        resultObj.put("rows",cityList);
        return resultObj;
    }

    @Override
    public Object getCityInfo() {

        JSONObject resultObj = new JSONObject();

        //先去redis中查询
        Object redisResult = redisCache.getCacheObject(CITY_INFO_ALL_INDEX);
        if (redisResult==null) {
            //先获取省与直辖市的信息
            LambdaQueryWrapper<CityCodeTbl> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(CityCodeTbl::getRegionLevel, 1);
            List<CityCodeTbl> provinceList = baseMapper.selectList(queryWrapper);
            //省与直辖市的存放集合
            Map<Integer, String> provinceMap = new HashMap<>();
            //用于存放省code的集合
            List<Integer> provinceCode = new ArrayList<>();

            Map<Integer, List<CityCodeInfoRespVo>> resultMap = new HashMap<>();

            //将省直辖市的行政编码放入到集合中方便取值
            provinceList.stream().forEach(province -> {
                provinceMap.put(province.getRegionCode(), province.getRegionName());
                provinceCode.add(province.getRegionCode());
                resultMap.put(province.getRegionCode(), new ArrayList<CityCodeInfoRespVo>());
            });

            //根据省code查询直辖的市信息
            LambdaQueryWrapper<CityCodeTbl> queryWrapperCity = Wrappers.lambdaQuery();
            queryWrapperCity.in(CityCodeTbl::getParentRegionCode, provinceCode);
            List<CityCodeTbl> cityCodeTbls = baseMapper.selectList(queryWrapperCity);
            cityCodeTbls.stream().forEach(cityCodeTbl -> {
                List<CityCodeInfoRespVo> cityCodeInfoRespVos = resultMap.get(cityCodeTbl.getParentRegionCode());
                cityCodeInfoRespVos.add(CityCodeInfoRespVo.builder().cityAreaCode(cityCodeTbl.getRegionCode())
                        .cityAreaName(cityCodeTbl.getRegionName()).build());
                resultMap.put(cityCodeTbl.getParentRegionCode(), cityCodeInfoRespVos);
            });
            JSONArray result = new JSONArray();
            provinceCode.stream().forEach(code -> {
                JSONObject resultMember = new JSONObject();
                String provinceName = provinceMap.get(code);
                resultMember.put("cityAreaCode", code);
                resultMember.put("cityAreaName", provinceName);
                resultMember.put("subBordList", resultMap.get(code));
                result.add(resultMember);
            });
            redisCache.setCacheObject(CITY_INFO_ALL_INDEX, result, 86400, TimeUnit.SECONDS);
            resultObj.put("code",200);
            resultObj.put("msg","查询成功");
            resultObj.put("rows",result);
            return resultObj;
        }
        resultObj.put("code",200);
        resultObj.put("msg","查询成功");
        resultObj.put("rows",redisResult);
        return resultObj;
    }



}
