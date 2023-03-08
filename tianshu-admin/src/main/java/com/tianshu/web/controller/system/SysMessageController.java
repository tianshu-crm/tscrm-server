package com.tianshu.web.controller.system;

import com.tianshu.common.core.controller.BaseController;
import com.tianshu.common.core.domain.AjaxResult;
import com.tianshu.common.core.page.TableDataInfo;
import com.tianshu.system.domain.vo.DeleteMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageReqVo;
import com.tianshu.system.domain.vo.QueryMessageRespVo;
import com.tianshu.system.service.ISysMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "系统消息管理")
@RestController
@RequestMapping("/message/v1")
public class SysMessageController extends BaseController {

    @Autowired
    private ISysMessageService iSysMessageService;

    @ApiOperation("消息中心-查询列表")
    @PostMapping("/list")
    public Object queryMessageList(@Validated @RequestBody QueryMessageReqVo request){
        return iSysMessageService.queryMessageList(request);
    }

    @ApiOperation("消息中心-删除")
    @PostMapping("/read/delete")
    public AjaxResult delete(@Validated @RequestBody DeleteMessageReqVo request){
        return iSysMessageService.deleteMessage(request);
    }

    @ApiOperation("消息中心消息已读")
    @PostMapping("/read/{messageId}")
    public AjaxResult readMessage(@PathVariable("messageId") String messageId){
        return iSysMessageService.readMessage(messageId);
    }

    @ApiOperation("消息中心全部已读")
    @PostMapping("/readall/{userId}")
    public AjaxResult readAllMessage(@PathVariable("userId")String userId){
        iSysMessageService.readAllMessage(userId);
        return AjaxResult.success();
    }

    @GetMapping("/notice/windowlist/{userId}")
    public TableDataInfo noticeWindowList(@PathVariable("userId")String userId){
        List<QueryMessageRespVo> queryMessageRespVos = iSysMessageService.noticeWindow(userId);
        return getDataTable(queryMessageRespVos);
    }
}
