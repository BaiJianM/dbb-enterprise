package com.gientech.iot.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: WebSocket会话缓存管理
 * @author: 白剑民
 * @dateTime: 2023/6/5 23:14
 */
@Slf4j
public class WebSocketSessionCache {

    /**
     * 会话唯一标识key格式化模板
     */
    private static final String KEY_FORMAT = "%s-%s";

    /**
     * 缓存所有会话信息
     */
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /**
     * @param socketType 业务类型
     * @param socketId   业务唯一标识
     * @param session    会话对象
     * @description: 添加会话缓存
     * @author: 白剑民
     * @date: 2023-06-05 23:18:06
     * @version: 1.0
     */
    public static void add(String socketType, String socketId, WebSocketSession session) {
        SESSIONS.put(keyFormat(socketType, socketId), session);
    }

    /**
     * @param socketType 业务类型
     * @param socketId   业务唯一标识
     * @description: 删除会话缓存
     * @author: 白剑民
     * @date: 2023-06-05 23:18:52
     * @return: org.springframework.web.socket.WebSocketSession
     * @version: 1.0
     */
    public static WebSocketSession remove(String socketType, String socketId) {
        return SESSIONS.remove(keyFormat(socketType, socketId));
    }

    /**
     * @param socketType 业务类型
     * @param socketId   业务唯一标识
     * @description: 删除会话缓存并关闭连接
     * @author: 白剑民
     * @date: 2023-06-05 23:19:25
     * @version: 1.0
     */
    public static void removeAndClose(String socketType, String socketId) {
        WebSocketSession session = remove(socketType, socketId);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param socketType 业务类型
     * @param socketId   业务唯一标识
     * @description: 获取缓存会话
     * @author: 白剑民
     * @date: 2023-06-05 23:20:04
     * @return: org.springframework.web.socket.WebSocketSession
     * @version: 1.0
     */
    public static WebSocketSession get(String socketType, String socketId) {
        return SESSIONS.get(keyFormat(socketType, socketId));
    }

    /**
     * @description: 获取当前会话连接数
     * @author: 白剑民
     * @date: 2023-06-05 23:20:38
     * @return: java.lang.Integer
     * @version: 1.0
     */
    public static Integer size() {
        return SESSIONS.size();
    }

    /**
     * @param socketType 业务类型
     * @param socketId   业务唯一标识
     * @description: 会话唯一标识格式化
     * @author: 白剑民
     * @date: 2023-06-05 23:17:33
     * @return: java.lang.String
     * @version: 1.0
     */
    private static String keyFormat(String socketType, String socketId) {
        return String.format(KEY_FORMAT, socketType, socketId);
    }
}
