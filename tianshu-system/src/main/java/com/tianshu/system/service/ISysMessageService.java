package com.tianshu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.system.domain.po.SysMessage;
import com.tianshu.system.domain.vo.DeleteMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageRespVo;

import java.util.List;


/**
 * 系统消息Service接口
 * 
 * @author ruoyi
 * @date 2023-01-05
 */
public interface ISysMessageService extends IService<SysMessage> {

    /**
     * 根据不同的筛选条件查询消息列表
     * @param request 请求vo
     * @return 消息列表
     */
    Object queryMessageList(QueryMessageReqVo request);

    /**
     * 删除消息
     * @param request 请求vo
     * @return 删除消息
     */
    AjaxResult deleteMessage(DeleteMessageReqVo request);

    void sendMesage(SysMessage sysMessage);

    /**
     * 设置为已读
     * @param id 消息id
     */
    AjaxResult readMessage(String id);

    /**
     * 通过用户id将全部消息已读
     * @param userId
     */
    void readAllMessage(String userId);

    /**
     * 消息提醒窗口
     * @param userId 用户id
     * @return 结果
     */
    List<QueryMessageRespVo> noticeWindow(String userId);
}
