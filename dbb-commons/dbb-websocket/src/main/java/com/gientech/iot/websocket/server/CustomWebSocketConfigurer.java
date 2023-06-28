package com.gientech.iot.websocket.server;

import com.gientech.iot.websocket.Interceptor.AbstractCustomHandshakeInterceptor;
import com.gientech.iot.websocket.Interceptor.DefaultCustomHandshakeInterceptor;
import com.gientech.iot.websocket.annotations.GientechWebSocketEndpoint;
import com.gientech.iot.websocket.handler.GientechWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @description: WebSocket处理器初始化
 * @author: 白剑民
 * @dateTime: 2023/06/05 11:27
 */
@Slf4j
public class CustomWebSocketConfigurer implements WebSocketConfigurer {

    /**
     * WebSocket请求路径前缀
     */
    private static final String REQUEST_PATH_PREFIX = "/";

    private final ApplicationContext context;

    public CustomWebSocketConfigurer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        // 获取所有实现了WebSocket处理器接口的实现类
        Map<String, GientechWebSocketHandler> handlers = context.getBeansOfType(GientechWebSocketHandler.class);
        // 校验这些实现类是否包含规定注解
        handlers.forEach((beanName, handler) -> {
            GientechWebSocketEndpoint annotation = handler.getClass().getAnnotation(GientechWebSocketEndpoint.class);
            if (annotation == null) {
                throw new RuntimeException(handler.getClass().getSimpleName() +
                        "实现了GientechWebSocketHandler接口，但是类上没有标注@GientechWebSocketEndpoint注解");
            }
            String path = annotation.value();
            if (!path.startsWith(REQUEST_PATH_PREFIX)) {
                throw new RuntimeException("WebSocket监听路径需以'/'开头");
            }
            // 注册WebSocket处理器
            registry
                    .addHandler(handler, path)
                    .addInterceptors(this.getHandshakeInterceptorImpl(annotation.handshakeInterceptors()))
                    .setAllowedOrigins("*");
        });
    }

    /**
     * @param arr 自定义WebSocket请求拦截器数组
     * @description: 获取自定义WebSocket请求拦截器数组实例
     * @author: 白剑民
     * @date: 2023-06-06 11:18:33
     * @return: org.springframework.web.socket.server.HandshakeInterceptor[]
     * @version: 1.0
     */
    private HandshakeInterceptor[] getHandshakeInterceptorImpl(
            Class<? extends AbstractCustomHandshakeInterceptor>[] arr) {
        HandshakeInterceptor[] interceptors = new HandshakeInterceptor[arr.length];
        for (int i = 0; i < arr.length; i++) {
            interceptors[i] = context.getBean(arr[i]);
        }
        if (interceptors.length == 0) {
            interceptors = new HandshakeInterceptor[1];
            interceptors[0] = new DefaultCustomHandshakeInterceptor();
        }
        return interceptors;
    }
}