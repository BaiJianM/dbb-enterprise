package top.dabaibai.websocket.handler;


import org.springframework.lang.NonNull;
import org.springframework.web.socket.*;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.http.SystemErrorCode;
import top.dabaibai.websocket.server.WebSocketSessionCache;

/**
 * @description: WebSocket消息处理器接口
 * @author: 白剑民
 * @dateTime: 2023/6/5 23:09
 */
public interface DbbWebSocketHandler extends WebSocketHandler {
    /**
     * 默认的redis广播消息监听频道
     */
    String WEBSOCKET_CHANNEL = "WebSocket-Channel";

    /**
     * @param socketType socket类型
     * @param socketId   socket唯一标识
     * @param session    socket会话
     * @description: 当websocket连接打开时执行
     * @author: 白剑民
     * @date: 2023-04-18 15:37:28
     * @version: 1.0
     */
    void onOpen(String socketType, String socketId, WebSocketSession session);

    /**
     * @param socketType socket类型
     * @param socketId   socket唯一标识
     * @param session    socket会话
     * @description: 当websocket连接关闭时执行
     * @author: 白剑民
     * @date: 2023-04-18 15:37:28
     * @version: 1.0
     */
    void onClose(String socketType, String socketId, WebSocketSession session);

    /**
     * @param socketType socket类型
     * @param socketId   socket唯一标识
     * @param message    websocket服务端接收到的消息
     * @param session    socket会话
     * @description: 当websocket服务端收到消息时执行
     * @author: 白剑民
     * @date: 2023-04-18 15:38:59
     * @version: 1.0
     */
    void onMessage(String socketType, String socketId, String message, WebSocketSession session);

    /**
     * @param socketType socket类型
     * @param socketId   socket唯一标识
     * @param session    socket会话
     * @param error      socket异常
     * @description: 当websocket连接产生异常时执行
     * @author: 白剑民
     * @date: 2023-04-18 15:40:11
     * @version: 1.0
     */
    void onError(String socketType, String socketId, WebSocketSession session, Throwable error);

    @Override
    default void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 业务类型
        String socketType = String.valueOf(session.getAttributes().get("socketType"));
        // 业务唯一标识
        String socketId = String.valueOf(session.getAttributes().get("socketId"));
        this.onOpen(socketType, socketId, session);
        WebSocketSessionCache.add(socketType, socketId, session);
    }

    @Override
    default void handleMessage(@NonNull WebSocketSession session,
                               @NonNull WebSocketMessage<?> message) throws Exception {
        // 业务类型
        String socketType = String.valueOf(session.getAttributes().get("socketType"));
        // 业务唯一标识
        String socketId = String.valueOf(session.getAttributes().get("socketId"));
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            this.onMessage(socketType, socketId, textMessage.getPayload(), session);
        } else {
            throw new DbbException(SystemErrorCode.UNSUPPORTED_WEBSOCKET_MESSAGE_TYPE);
        }
    }

    @Override
    default void afterConnectionClosed(@NonNull WebSocketSession session,
                                       @NonNull CloseStatus closeStatus) throws Exception {
        // 业务类型
        String socketType = String.valueOf(session.getAttributes().get("socketType"));
        // 业务唯一标识
        String socketId = String.valueOf(session.getAttributes().get("socketId"));
        this.onClose(socketType, socketId, session);
        WebSocketSessionCache.remove(socketType, socketId);
    }

    @Override
    default void handleTransportError(@NonNull WebSocketSession session,
                                      @NonNull Throwable exception) throws Exception {
        // 业务类型
        String socketType = String.valueOf(session.getAttributes().get("socketType"));
        // 业务唯一标识
        String socketId = String.valueOf(session.getAttributes().get("socketId"));
        this.onError(socketType, socketId, session, exception);
        WebSocketSessionCache.removeAndClose(socketType, socketId);
    }

    @Override
    default boolean supportsPartialMessages() {
        return false;
    }
}
