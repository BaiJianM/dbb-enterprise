package com.gientech.iot.websocket.operation;

import com.gientech.iot.websocket.server.GientechWebSocketSender;
import com.gientech.iot.websocket.server.WebSocketSessionCache;
import com.gientech.iot.websocket.vo.WebSocketMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @description: WebSocket消息发送抽象类
 * @author: 白剑民
 * @dateTime: 2023/4/18 13:39
 */
@Slf4j
public abstract class AbstractWebSocketSender implements GientechWebSocketSender {

    @Override
    public boolean sendMessage(WebSocketMessageVO message) {
        boolean result = false;
        WebSocketSession session = WebSocketSessionCache.get(message.getSocketType(), message.getSocketId());
        if (session != null) {
            synchronized (this) {
                try {
                    TextMessage textMessage = new TextMessage(message.getContent());
                    session.sendMessage(textMessage);
                    result = true;
                } catch (IOException e) {
                    log.error("默认单节点WebSocket消息发送出错，错误信息: {}", e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * @param message 分布式websocket消息体
     * @description: 分布式websocket消息发送实现
     * @author: 白剑民
     * @date: 2023-04-18 15:53:18
     * @return: boolean
     * @version: 1.0
     */
    public abstract boolean sendMessageByDistributed(WebSocketMessageVO message);
}
