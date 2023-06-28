package top.dabaibai.websocket.annotations;

import top.dabaibai.websocket.Interceptor.AbstractCustomHandshakeInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: WebSocket监听服务端点
 * @author: 白剑民
 * @dateTime: 2023/06/05 11:27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DbbWebSocketEndpoint {

    /**
     * @description: WebSocket监听路径
     * @author: 白剑民
     * @date: 2023-06-05 23:37:18
     * @return: java.lang.String
     * @version: 1.0
     */
    String value();

    /**
     * @description: 自定义WebSocket请求拦截器
     * @author: 白剑民
     * @date: 2023-06-06 14:27:53
     * @return: java.lang.Class<? extends top.dabaibai.websocket.Interceptor.AbstractCustomHandshakeInterceptor>[]
     * @version: 1.0
     */
    Class<? extends AbstractCustomHandshakeInterceptor>[] handshakeInterceptors() default {};

}
