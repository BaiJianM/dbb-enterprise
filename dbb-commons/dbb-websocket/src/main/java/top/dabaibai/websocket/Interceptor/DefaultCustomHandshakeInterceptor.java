package top.dabaibai.websocket.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import top.dabaibai.websocket.server.WebSocketSessionCache;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 默认的自定义WebSocket请求拦截器增强类
 * @author: 白剑民
 * @dateTime: 2023/6/6 10:26
 */
@Slf4j
public class DefaultCustomHandshakeInterceptor extends AbstractCustomHandshakeInterceptor {
    @Override
    public boolean beforeHandshakeEx(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes,
                                   boolean isConnectAlreadyExist) {
        if (isConnectAlreadyExist) {
            String socketType = attributes.get("socketType").toString();
            String socketId = attributes.get("socketId").toString();
            WebSocketSession session = WebSocketSessionCache.get(socketType, socketId);
            TextMessage message = new TextMessage("检测到新的相同会话连接，当前连接已断开");
            try {
                session.sendMessage(message);
                session.close();
            } catch (IOException e) {
                log.error("默认的WebSocket增强拦截器报错: {}", e.getMessage());
            }
        }
        return true;
    }
}
