package com.gientech.iot.demo.biz.service.impl.websocket;

import com.gientech.iot.websocket.Interceptor.AbstractCustomHandshakeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/8 23:26
 */
@Slf4j
@Component
public class DemoWebSocketInterceptor2 extends AbstractCustomHandshakeInterceptor {
    @Override
    public boolean beforeHandshakeEx(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes,
                                   boolean isConnectAlreadyExist) {
        log.info("我是抽象的WebSocket拦截器-2");
        return true;
    }
}
