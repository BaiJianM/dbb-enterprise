package com.gientech.iot.websocket.Interceptor;

import cn.hutool.http.HttpUtil;
import com.gientech.iot.websocket.server.WebSocketSessionCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @description: WebSocket请求拦截器，继承此类需将类设置为有效的Spring Bean
 * @author: 白剑民
 * @dateTime: 2023/6/5 23:00
 */
@Slf4j
public abstract class AbstractCustomHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     *
     * @param request    请求
     * @param response   响应
     * @param wsHandler  ws处理程序
     * @param attributes ws参数
     * @return boolean
     */
    @Override
    public final boolean beforeHandshake(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8);
        String ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        // 业务类型
        String socketType = paramMap.get("socketType");
        // 业务唯一标识
        String socketId = paramMap.get("socketId");
        if (socketType == null || socketType.length() == 0
                || socketId == null || socketId.length() == 0) {
            log.error("WebSocket请求需包含业务类型[socketType]、业务唯一标识[socketId]参数");
            return false;
        }
        attributes.put("socketType", socketType);
        attributes.put("socketId", socketId);
        attributes.put("ipAddress", ipAddress);
        // 判断WebSocket连接是否已经存在
        boolean isConnectAlreadyExist = WebSocketSessionCache.get(socketType, socketId) != null;
        // 调用抽象方法增强逻辑
        return this.beforeHandshakeEx(request, response, wsHandler, attributes, isConnectAlreadyExist);
    }

    /**
     * @param request               连接请求对象
     * @param response              连接响应对象
     * @param wsHandler             处理器
     * @param attributes            socket属性映射
     * @param isConnectAlreadyExist 该WebSocket连接是否已经存在，需子类自己实现已存在连接时的逻辑
     * @description: 握手前增强方法
     * @author: 白剑民
     * @date: 2023-06-06 09:35:36
     * @return: boolean
     * @version: 1.0
     */
    public abstract boolean beforeHandshakeEx(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                            @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes,
                                            boolean isConnectAlreadyExist);

    /**
     * 握手后
     *
     * @param request   请求
     * @param response  响应
     * @param wsHandler ws处理程序
     * @param exception 异常
     */
    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler, Exception exception) {
    }

}