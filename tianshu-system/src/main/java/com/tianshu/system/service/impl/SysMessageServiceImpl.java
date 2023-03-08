package com.tianshu.system.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.domain.entity.SysDictData;
import com.tianshu.common.core.text.Convert;
import com.tianshu.common.utils.SecurityUtils;
import com.tianshu.system.websocket.WebSocketUtil;
import com.tianshu.system.domain.po.SysMessage;
import com.tianshu.system.domain.vo.DeleteMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageRespVo;
import com.tianshu.system.mapper.SysDictDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianshu.system.mapper.SysMessageMapper;

import com.tianshu.system.service.ISysMessageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 系统消息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-05
 */
@Service
@Slf4j
@Transactional
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    @Autowired
    private WebSocketUtil webSocketUtil;

    @Autowired
    SysDictDataMapper sysDictDataMapper;

    @Override
    public Object queryMessageList(QueryMessageReqVo request) {
        log.info("查询消息列表接口入参:{}", JSON.toJSONString(request));
        //设置查询条件
        LambdaQueryWrapper<SysMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysMessage::getUserId, request.getUserId());
        queryWrapper.eq(SysMessage::getDelFlag, "0");
        //查询类别 0已读 1未读 2全部
        if (StringUtils.isNotBlank(request.getStatus())) {
            //对查询类别进行分类 传入的是0或1才会将数据写入sql，否则不写入差全部
            switch (request.getStatus()) {
                case "0":
                case "1":
                    queryWrapper.eq(SysMessage::getStatus, request.getStatus());
                    break;
                default:
                    break;
            }
        }
        //对消息类别进行分类
        if (StringUtils.isNotBlank(request.getMessageType())) {
            //对消息类别进行分类 属于业务标记的 消息类别 1任务消息 2系统消息 3审批消息 写入sql,不属于的不能写入sql
            switch (request.getMessageType()) {
                case "1":
                case "2":
                case "3":
                case "4":
                    queryWrapper.eq(SysMessage::getType, request.getMessageType());
                default:
                    break;
            }
        }
        //根据必要必要条件查询数据的条数
        Long total = baseMapper.selectCount(queryWrapper);
        queryWrapper.orderByDesc(SysMessage::getSendTime);
        Page<SysMessage> page = new Page<>();
        page.setCurrent(request.getPageNum());
        page.setSize(request.getPageSize());

        Page<SysMessage> pageResult = baseMapper.selectPage(page, queryWrapper);
        log.info("查询消息列表查询结果:{}", JSON.toJSONString(pageResult.getRecords()));
        return mapperResp(pageResult.getRecords(), total,request.getUserId(),request.getStatus());
    }

    /**
     * 用来构建返回vo
     *
     * @param dataBaseResult 数据库查询的数据
     * @param total          数据的总条数
     * @param userId  用户id
     * @param status 数据是否已读状态
     * @return 返回的结果
     */
    private JSONObject mapperResp(List<SysMessage> dataBaseResult, long total,String userId,String status) {
        JSONObject result = new JSONObject();
        List<QueryMessageRespVo> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataBaseResult)) {
            result.put("rows", resultList);
            result.put("total", 0);
            result.put("code", 200);
            result.put("msg", "查询成功");
            return queryMessageTypeCount(result,userId,status);
        }
        //构建返回的vo
        dataBaseResult.stream().forEach(sysMessage -> {
            QueryMessageRespVo queryMessageRespVo = QueryMessageRespVo.builder()
                    .id(sysMessage.getId())
                    .title(sysMessage.getTitle())
                    .type(sysMessage.getType())
                    .sendTime(DateUtil.format(sysMessage.getSendTime(),"yyyy-MM-dd HH:mm"))
                    .userId(sysMessage.getUserId())
                    .detail(sysMessage.getDetail())
                    .status(sysMessage.getStatus()).build();

            switch (sysMessage.getType()) {
                case "1":
                    queryMessageRespVo.setType("任务消息");
                    break;
                case "2":
                default:
                    queryMessageRespVo.setType("系统消息");
                    break;
                case "3":
                    queryMessageRespVo.setType("审批消息");
                    break;
                case "4":
                    queryMessageRespVo.setType("进度提醒");
            }
            switch (sysMessage.getApproval()) {
                case "1":
                    queryMessageRespVo.setApproval("已审批");
                    break;
                case "2":
                    queryMessageRespVo.setApproval("未审批");
                    break;
                case "3":
                    queryMessageRespVo.setApproval("已驳回");
                    break;
                case "4":
                default:
                    queryMessageRespVo.setApproval("不需要审批");
                    break;
            }
            resultList.add(queryMessageRespVo);
        });
        result.put("rows", resultList);
        result.put("total", total);
        result.put("code", 200);
        result.put("msg", "查询成功");

        return queryMessageTypeCount(result,userId,status);
    }

    /**
     * 查询各类型消息的数量
     *
     * @param jsonObject 包含结果集的数据
     * @param userId     用户id
     * @param status     消息状态
     * @return 接口返回值
     */
    private JSONObject queryMessageTypeCount(JSONObject jsonObject, String userId,String status) {

        boolean flag = false;
        if ("0".equals(status)||"1".equals(status)){
            flag=true;
        }

        LambdaQueryWrapper<SysMessage> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.eq(SysMessage::getUserId, userId);
        if (flag){
            queryWrapper1.eq(SysMessage::getStatus,status);
        }
        queryWrapper1.eq(SysMessage::getType, "1");
        queryWrapper1.eq(SysMessage::getDelFlag, "0");
        Long taskMsgCount = baseMapper.selectCount(queryWrapper1);

        LambdaQueryWrapper<SysMessage> queryWrapper2 = Wrappers.lambdaQuery();
        queryWrapper2.eq(SysMessage::getUserId, userId);
        if (flag){
            queryWrapper2.eq(SysMessage::getStatus,status);
        }
        queryWrapper2.eq(SysMessage::getType, "2");
        queryWrapper2.eq(SysMessage::getDelFlag, "0");
        Long sysMsgCount = baseMapper.selectCount(queryWrapper2);

        LambdaQueryWrapper<SysMessage> queryWrapper3 = Wrappers.lambdaQuery();
        queryWrapper3.eq(SysMessage::getUserId, userId);
        if (flag){
            queryWrapper3.eq(SysMessage::getStatus,status);
        }
        queryWrapper3.eq(SysMessage::getType, "3");
        queryWrapper3.eq(SysMessage::getDelFlag, "0");
        Long approvalCount = baseMapper.selectCount(queryWrapper3);

        LambdaQueryWrapper<SysMessage> queryWrapper4 = Wrappers.lambdaQuery();
        queryWrapper4.eq(SysMessage::getUserId, userId);
        if (flag){
            queryWrapper4.eq(SysMessage::getStatus,status);
        }
        queryWrapper4.eq(SysMessage::getType, "4");
        queryWrapper4.eq(SysMessage::getDelFlag, "0");
        Long tpCount = baseMapper.selectCount(queryWrapper4);


        //将查询回来的数据条数值写入返回值中
        jsonObject.put("taskMsgCount",taskMsgCount);
        jsonObject.put("sysMsgCount",sysMsgCount);
        jsonObject.put("approvalCount",approvalCount);
        jsonObject.put("tpCount",tpCount);
        return jsonObject;
    }


    @Override
    public AjaxResult deleteMessage(DeleteMessageReqVo request) {
        log.info("消息列表删除接口:{}", JSON.toJSONString(request));

        AjaxResult result = new AjaxResult();
        //根据删除类型区别处理
        switch (request.getIsDeleteAll()) {
            //非全部删除
            case 0:
                if (CollectionUtils.isEmpty(request.getMessageId())) {
                    result = AjaxResult.error("待删除的消息组为空");
                    break;
                }
                result = mesDeleteOther(request);
                break;
            case 1:
                result = mesDeleteAll(request);
                break;
        }
        return result;
    }

    /**
     * 删除全部消息
     *
     * @param request 删除请求vo
     * @return 删除结果
     */
    private AjaxResult mesDeleteAll(DeleteMessageReqVo request) {
        AjaxResult result = new AjaxResult();
        //先查询待删除的有多少条
        LambdaQueryWrapper<SysMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysMessage::getUserId, request.getUserId());
        queryWrapper.eq(SysMessage::getStatus, request.getStatus());
        queryWrapper.eq(SysMessage::getDelFlag, "0");
        Long aLong = baseMapper.selectCount(queryWrapper);
        //根据用户id 与已读状态删除所有没有删除的消息
        LambdaUpdateWrapper<SysMessage> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysMessage::getDelFlag, "1");
        updateWrapper.set(SysMessage::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SysMessage::getUpdateTime, new Date());
        updateWrapper.eq(SysMessage::getUserId, request.getUserId());
        updateWrapper.eq(SysMessage::getStatus, request.getStatus());
        updateWrapper.eq(SysMessage::getDelFlag, "0");
        long update = baseMapper.update(null, updateWrapper);
        if (update != aLong.longValue()) {
            return AjaxResult.error("待删除条数与实际删除条数不一致");

        }
        return AjaxResult.success();
    }

    /**
     * 删除特定消息
     *
     * @param request 删除选中的消息
     * @return 删除结果
     */
    private AjaxResult mesDeleteOther(DeleteMessageReqVo request) {
        //根据用户id 数据id 未读状态 没有被删除
        LambdaUpdateWrapper<SysMessage> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysMessage::getDelFlag, "1");
        updateWrapper.set(SysMessage::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SysMessage::getUpdateTime, new Date());
        updateWrapper.eq(SysMessage::getUserId, request.getUserId());
        updateWrapper.eq(SysMessage::getDelFlag, "0");
        updateWrapper.in(SysMessage::getId, request.getMessageId());
        int update = baseMapper.update(null, updateWrapper);
        //若待删除条数
        if (request.getMessageId().size() != update) {
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success();
    }


    @Override
    public void sendMesage(SysMessage sysMessage) {
        /**
         * 1.webSocketUtil 发送消息
         * 2.保存消息主体
         */
        try {
            webSocketUtil.sendMessageByUserId(Convert.toStr(sysMessage.getUserId()),sysMessage.getTitle());
        } catch (IOException e) {
            throw new RuntimeException("发送消息失败！",e);
        }
        sysMessage.setStatus("1");
        sysMessage.setSendTime(new Date());
        sysMessage.setDetail(new JSONObject().toJSONString());
        sysMessage.setCreateInfo(SecurityUtils.getUsername());
        this.save(sysMessage);
    }

    @Override
    public AjaxResult readMessage(String messageId) {
        LambdaUpdateWrapper<SysMessage> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysMessage::getStatus,"0");
        updateWrapper.eq(SysMessage::getId,Long.parseLong(messageId));
        baseMapper.update(null, updateWrapper);
        return AjaxResult.success();
    }

    @Override
    public void readAllMessage(String userId) {
        LambdaUpdateWrapper<SysMessage> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SysMessage::getStatus,"0");
        updateWrapper.eq(SysMessage::getUserId,userId);
        baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<QueryMessageRespVo> noticeWindow(String userId) {
        //获取数据词典的label与value
        List<SysDictData> crmMessageType = sysDictDataMapper.queryMsgType();
        Map<String, String> messageLabel = crmMessageType.stream()
                .collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
        //根据用户id查询消息列表倒序前5条
        LambdaQueryWrapper<SysMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysMessage::getUserId,userId);
        queryWrapper.orderByDesc(SysMessage::getSendTime);
        //倒序后 查询前5条
        Page<SysMessage> page = new Page<>();
        page.setCurrent(0);
        page.setSize(5);
        Page<SysMessage> pageResult = baseMapper.selectPage(page, queryWrapper);
        List<SysMessage> records = pageResult.getRecords();
        //对标题进行拼接返回
        List<QueryMessageRespVo> result = new ArrayList<>();
        records.stream().forEach(record->{
            QueryMessageRespVo ment = QueryMessageRespVo.builder()
                    .id(record.getId())
                    .type(messageLabel.get(record.getType()))
                    .title("【" + messageLabel.get(record.getType()) + "】" + record.getTitle())
                    .status(record.getStatus())
                    .userId(record.getUserId()).build();
            result.add(ment);
        });
        return result;
    }


}
