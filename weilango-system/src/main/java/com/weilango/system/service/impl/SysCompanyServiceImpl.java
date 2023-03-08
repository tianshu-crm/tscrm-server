package com.weilango.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilango.common.core.domain.AjaxResult;
import com.weilango.common.core.domain.entity.SysDept;
import com.weilango.common.core.page.TableDataInfo;
import com.weilango.common.utils.RandomCodeUtil;
import com.weilango.common.utils.SecurityUtils;
import com.weilango.system.domain.vo.SysCompanyAddReqVo;
import com.weilango.system.domain.vo.SysCompanyReqVo;
import com.weilango.system.domain.vo.SysCompanyRespVo;
import com.weilango.system.domain.vo.SysCompanyUpdateReqVo;
import com.weilango.system.mapper.SysCompanyMapper;
import com.weilango.system.mapper.SysDeptMapper;
import com.weilango.system.mapper.SysFilialeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weilango.system.domain.SysCompany;
import com.weilango.system.service.SysCompanyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 公司管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-05
 */
@Service
@Slf4j
@Transactional
public class SysCompanyServiceImpl extends ServiceImpl<SysCompanyMapper, SysCompany> implements SysCompanyService {
    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysFilialeMapper filialeMapper;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询公司管理
     *
     * @param companyId 公司管理主键
     * @return 公司管理
     */
    @Override
    public SysCompany selectSysCompanyById(String companyId) {
        return sysCompanyMapper.selectSysCompanyById(companyId);
    }

    /**
     * 查询公司管理列表
     *
     * @param request 公司管理
     * @return 公司管理
     */
    @Override
    public List<SysCompany> selectSysCompanyList(SysCompanyReqVo request) {
        return null;
    }

    @Override
    public TableDataInfo querySysCompanyList(SysCompanyReqVo request) {
        log.info("公司列表查询请求入参:{}",JSON.toJSONString(request));
        //构建查询驱动对象
        LambdaQueryWrapper<SysCompany> queryWrapper = Wrappers.lambdaQuery();
        //有效的公司
        queryWrapper.eq(SysCompany::getDelFlag,"0");
        if (StringUtils.isNotBlank(request.getCompanyName())) {
            //公司名称模糊查询
            queryWrapper.like(SysCompany::getCompanyName, request.getCompanyName());
        }
        //数据库创建时间倒序排序
        queryWrapper.orderByDesc(SysCompany::getCreateTime);
        Long total = baseMapper.selectCount(queryWrapper);
        //构建分页对象
        Page<SysCompany> page = new Page<>();
        page.setCurrent(request.getPageNum());
        page.setSize(request.getPageSize());
        Page<SysCompany> pageResult = baseMapper.selectPage(page,queryWrapper);
        List<SysCompany> dataBaseResultList = pageResult.getRecords();
        //创建数据集合容器
        List<SysCompanyRespVo> resultList = new ArrayList<>();
        //遍历数据库原生数据，构建返回数据
        if (!CollectionUtils.isEmpty(dataBaseResultList)){
            dataBaseResultList.stream().forEach(member ->{
                SysCompanyRespVo sysCompanyRespVo = SysCompanyRespVo.builder()
                        .companyId(member.getCompanyId())
                        .cityCode(member.getCityCode())
                        .cityName(member.getCityName())
                        .companyName(member.getCompanyName())
                        .companyRegAddress(member.getCompanyRegAddress())
                        .companyComAddress(member.getCompanyComAddress())
                        .companyRegTime(member.getCompanyRegTime())
                        .companyPhone(member.getCompanyPhone())
                        .regCapital(member.getRegCapital())
                        .identifier(member.getIdentifier())
                        .staffNum(member.getStaffNum())
                        .legalPersonName(member.getLegalPersonName())
                        .legalPersonIdcard(member.getLegalPersonIdcard())
                        .legalPersonPhone(member.getLegalPersonPhone())
                        .existBranchCompany(member.getExistBranchCompany())
                        .branchCompanyNum(member.getBranchCompanyNum())
                        .accountName(member.getAccountName())
                        .accountOpenBank(member.getAccountOpenBank())
                        .accountNum(member.getAccountNum())
                        .accountPhone(member.getAccountPhone())
                        .dataCreateTime(simpleDateFormat.format(member.getCreateTime())).build();
                resultList.add(sysCompanyRespVo);
            });
        }
        //构建返回值容器
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        tableDataInfo.setRows(resultList);
        tableDataInfo.setTotal(total);
        //开始查询
        return tableDataInfo;
    }

    /**
     * 新增公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    @Override
    public int insertSysCompany(SysCompany sysCompany) {
        sysCompany.setCreateInfo(SecurityUtils.getUsername());
        return sysCompanyMapper.insertSysCompany(sysCompany);

    }

    @Transactional
    @Override
    public AjaxResult addSysCompany(SysCompanyAddReqVo request){
        log.info("新增公司接口入参：{}",JSON.toJSONString(request));
        //创建返回值载体对象
         JSONObject result = new JSONObject();
        //构建入库po
        SysCompany sysCompany = new SysCompany();
        //公司序列id
        sysCompany.setCompanyId(RandomCodeUtil.getDataIdLong());
        //公司名称
        sysCompany.setCompanyName(request.getCompanyName());
        //城市行政划分代码
        sysCompany.setCityCode(request.getCityCode());
        //所在城市
        sysCompany.setCityName(request.getCityName());
        //法人代表
        sysCompany.setLegalPersonName(request.getLegalPersonName());
        //公司联系电话
        sysCompany.setCompanyPhone(request.getCompanyPhone());
        //公司注册地址
        sysCompany.setCompanyRegAddress(request.getCompanyRegAddress());
        //公司注册时间
        sysCompany.setCompanyRegTime(request.getCompanyRegTime());
        //开户行账号
        sysCompany.setAccountNum(request.getAccountNum());
        //开户行
        sysCompany.setAccountOpenBank(request.getAccountOpenBank());

        sysCompany.setCityCodeList(JSON.toJSONString(request.getCityCodeList()));
        //社会信用代码
        sysCompany.setIdentifier(request.getIdentifier());
        sysCompany.setCreateInfo(SecurityUtils.getUsername());
        baseMapper.insert(sysCompany);
        addCompanyToDept(sysCompany);
        return AjaxResult.success();
    }

    /**
     * 公司视为最大的一个部门 本方法用于在添加公司的时候添加部门
     * @param sysCompany
     */
    private void addCompanyToDept(SysCompany sysCompany){
        SysDept sysDept = new SysDept();
        sysDept.setParentId(0l);
        sysDept.setAncestors("0");
        sysDept.setDeptCompanyId(sysCompany.getId());
        sysDept.setDeptName(sysCompany.getCompanyName());
        sysDept.setOrderNum(0);
        sysDept.setLeader(sysCompany.getLegalPersonName());
        sysDept.setPhone(sysCompany.getCompanyPhone());
        sysDept.setStatus("0");
        sysDept.setDelFlag("0");
        sysDept.setCreateInfo(SecurityUtils.getUsername());
        sysDeptMapper.insertDept(sysDept);
    }

    @Override
    public AjaxResult updateCompany(SysCompanyUpdateReqVo request) {
        log.info("公司信息修改接口如参:{}",JSON.toJSONString(request));
        //创建数据修改对象
        LambdaUpdateWrapper<SysCompany> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysCompany::getCompanyName,request.getCompanyName());
        updateWrapper.set(SysCompany::getCompanyRegAddress,request.getCompanyRegAddress());
        updateWrapper.set(SysCompany::getLegalPersonName,request.getLegalPersonName());
        updateWrapper.set(SysCompany::getCompanyPhone,request.getCompanyPhone());
        updateWrapper.set(SysCompany::getCityName,request.getCityName());
        updateWrapper.set(SysCompany::getCityCode,request.getCityCode());
        updateWrapper.set(SysCompany::getIdentifier,request.getIdentifier());
        updateWrapper.set(SysCompany::getAccountNum,request.getAccountNum());
        updateWrapper.set(SysCompany::getCompanyRegTime,request.getCompanyRegTime());
        updateWrapper.set(SysCompany::getAccountOpenBank,request.getAccountOpenBank());
        updateWrapper.set(SysCompany::getUpdateBy,SecurityUtils.getUsername());
        updateWrapper.set(SysCompany::getCityCodeList,JSON.toJSONString(request.getCityCodeList()));
        //修改人
        updateWrapper.set(SysCompany::getUpdateTime,new Date());
        updateWrapper.eq(SysCompany::getCompanyId,request.getCompanyId());
        //创建返回值载体
        int i = baseMapper.update(null, updateWrapper);
        if (i==0){
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Transactional
    @Override
    public AjaxResult deleteCompany(Long companyId) {
        log.info("删除公司信息，操作人{},待删除公司id{}",SecurityUtils.getUsername(),companyId);
        LambdaUpdateWrapper<SysCompany> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysCompany::getDelFlag,"1");
        updateWrapper.set(SysCompany::getUpdateBy,SecurityUtils.getUsername());
        updateWrapper.set(SysCompany::getUpdateTime,new Date());
        updateWrapper.eq(SysCompany::getCompanyId,companyId);
        int companyDel = baseMapper.update(null, updateWrapper);
        //创建返回值载体
        JSONObject result = new JSONObject();
        if (companyDel==0){
            return AjaxResult.error();
        }
        //todo 连着删除分公司
//        filialeMapper.deleteSysFilialeByCompanyId(companyId);
        return AjaxResult.success();
    }


    /**
     * 修改公司管理
     *
     * @param sysCompany 公司管理
     * @return 结果
     */
    @Override
    public int updateSysCompany(SysCompany sysCompany) {
        return sysCompanyMapper.updateSysCompany(sysCompany);
    }

    /**
     * 批量删除公司管理
     *
     * @param ids 需要删除的公司管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCompanyByIds(Long[] ids) {
        return sysCompanyMapper.deleteSysCompanyByIds(ids);
    }

    /**
     * 删除公司管理信息
     *
     * @param id 公司管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCompanyById(Long id) {
        return sysCompanyMapper.deleteSysCompanyById(id);
    }

    @Override
    public Object getCompanyList() {
        //创建返回值
        JSONObject result = new JSONObject();
        //查询全部公司 公司名称 公司id
        List<SysCompany> sysCompanies = sysCompanyMapper.selectAll();
        //创建存放公司的数组
        JSONArray jsonArray = new JSONArray();
        sysCompanies.stream().forEach(sysCompany -> {
            JSONObject member = new JSONObject();
            member.put("dictLabel",sysCompany.getCompanyName());
            member.put("dictValue",sysCompany.getId().toString());
            jsonArray.add(member);
        });
        //todo 暂时不做修改，需要前端配合
        result.put("code",200);
        result.put("msg","查询成功");
        result.put("rows",jsonArray);
        return result;
    }


}
