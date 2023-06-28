package top.dabaibai.demo.biz.service.impl.websocket;

import top.dabaibai.websocket.annotations.DbbWebSocketEndpoint;
import top.dabaibai.websocket.handler.DbbWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * @description: 使用自定义封装过的WebSocket实现
 * @author: 白剑民
 * @dateTime: 2023/6/6 00:01
 */
@Slf4j
@DbbWebSocketEndpoint(value = "/test",
        handshakeInterceptors = {DemoWebSocketInterceptor1.class, DemoWebSocketInterceptor2.class})
public class DemoWebSocketHandler implements DbbWebSocketHandler {
    @Override
    public void onOpen(String socketType, String socketId, WebSocketSession session) {
        log.info("啊啊啊");
    }

    @Override
    public void onClose(String socketType, String socketId, WebSocketSession session) {

    }

    @Override
    public void onMessage(String socketType, String socketId, String message, WebSocketSession session) {

    }

    @Override
    public void onError(String socketType, String socketId, WebSocketSession session, Throwable error) {

    }
}
