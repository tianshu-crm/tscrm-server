package com.tianshu.web.controller.websocket;


import com.alibaba.fastjson2.JSONObject;
import com.tianshu.system.domain.vo.WebsocketReqVo;
import com.tianshu.system.websocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/websocket/v1")
public class WebSocketController {

    @Autowired
    private WebSocketUtil webSocketUtil;

    @PostMapping("/notice/userid")
    public Object sendMessageByUserId(@Validated @RequestBody WebsocketReqVo request){
        log.info("websocket 向{}用户发送消息,消息内容:{}",request.getUserId(),request.getNotice());
        JSONObject result = new JSONObject();
        try {
            webSocketUtil.sendMessageByUserId(request.getUserId(),request.getNotice());
        } catch (IOException e) {
            log.error("消息发送失败{}",e.getMessage());
            result.put("code",400);
            result.put("msg","消息发送失败");
            return result;
        }
        result.put("code",200);
        result.put("msg","操作成功");
        return result;
    }
}
