package com.gientech.iot.websocket.server;

import com.gientech.iot.websocket.vo.WebSocketMessageVO;

/**
 * @description: WebSocket消息发送接口
 * @author: 白剑民
 * @dateTime: 2023/6/5 23:09
 */
public interface GientechWebSocketSender {
    /**
     * @param message socket消息体
     * @description: 服务端主动向客户端发送消息
     * @author: 白剑民
     * @date: 2023-04-18 15:41:13
     * @return: boolean
     * @version: 1.0
     */
    boolean sendMessage(WebSocketMessageVO message);
}
