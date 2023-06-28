package com.gientech.iot.websocket.listener;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.redis.annotation.RedisChannel;
import com.gientech.iot.websocket.handler.GientechWebSocketHandler;
import com.gientech.iot.websocket.server.WebSocketSessionCache;
import com.gientech.iot.websocket.vo.WebSocketMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @description: 基于redis广播消息实现的分布式websocket消息监听器
 * @author: 白剑民
 * @dateTime: 2023/4/18 13:50
 */
@Slf4j
@RedisChannel(name = GientechWebSocketHandler.WEBSOCKET_CHANNEL)
public class DistributedWebSocketListener implements MessageListener {
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        // 序列化消息体
        String msg = JSON.parseObject(new String(message.getBody(), StandardCharsets.UTF_8), String.class);
        if (msg.length() > 0) {
            WebSocketMessageVO messageVO = JSON.parseObject(msg, WebSocketMessageVO.class);
            // 判断当前服务节点有无Session缓存
            WebSocketSession session = WebSocketSessionCache.get(messageVO.getSocketType(), messageVO.getSocketId());
            // 如果有则执行消息发送至目标websocket连接上
            if (session != null) {
                // 在高并发下可能出现session被多个线程共用导致socket状态异常的问题，这里需要加同步锁
                synchronized (this) {
                    try {
                        TextMessage textMessage = new TextMessage(messageVO.getContent());
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error("分布式WebSocket消息发送出错，错误信息: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
