package com.tianshu.system.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketUtil {
    private final static Logger logger = LogManager.getLogger(WebSocketUtil.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */

    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象
     */
    private static ConcurrentHashMap<String, WebSocketUtil> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */

    private Session session;
    private String userId;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        //加入map
        session.setMaxIdleTimeout(7200000);
        webSocketMap.put(userId, this);
        addOnlineCount();           //在线数加1
        logger.info("用户{}连接成功,当前在线人数为{}", userId, getOnlineCount());
        try {
            sendMessage(String.valueOf(this.session.getQueryString()));
        } catch (IOException e) {
            logger.error("IO异常");
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        webSocketMap.remove(userId);
        subOnlineCount();           //在线数减1
        logger.info("用户{}关闭连接！当前在线人数为{}", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端用户：{} 消息:{}", userId, message);
    }

    /**
     * 发生错误时调用
     *
     * @OnError
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 通过userId向客户端发送消息
     */
    public void sendMessageByUserId(String userId, Object notice) throws IOException {
        logger.info("服务端发送消息到{},消息：{}", userId, JSON.toJSONString(notice));
        if (StrUtil.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            //对发送方的消息进行再处理
            JSONObject noticeObj = new JSONObject();
            noticeObj.put("notice",notice);
            noticeObj.put("flag",true);
            webSocketMap.get(userId).sendMessage(JSON.toJSONString(noticeObj));
        } else {
            logger.error("用户{}不在线", userId);
        }

    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (String item : webSocketMap.keySet()) {
            try {
                webSocketMap.get(item).sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketUtil.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketUtil.onlineCount--;
    }

}