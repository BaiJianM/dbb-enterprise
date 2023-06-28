package top.dabaibai.websocket;

import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.websocket.listener.DistributedWebSocketListener;
import top.dabaibai.websocket.operation.DefaultWebSocketSender;
import top.dabaibai.websocket.server.CustomWebSocketConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @description: websocket自动配置类
 * @author: 白剑民
 * @dateTime: 2023/4/15 09:25
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketAutoConfiguration {

    /**
     * @description: websocket服务配置, 将自动扫描@ServerEndpoint
     * @author: 白剑民
     * @date: 2022-07-20 08:29:21
     * @return: org.springframework.web.socket.server.standard.ServerEndpointExporter
     * @version: 1.0
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * @description: 默认的websocket消息发送操作类
     * @author: 白剑民
     * @date: 2023-04-18 10:38:34
     * @return: top.dabaibai.websocket.server.DbbWebSocketSender
     * @version: 1.0
     */
    @Bean
    public DefaultWebSocketSender defaultWebSocketSender(RedisUtils redisUtils) {
        return new DefaultWebSocketSender(redisUtils);
    }

    /**
     * @description: 当默认的websocket消息发送操作类被装配时，再装配分布式websocket消息监听器
     * @author: 白剑民
     * @date: 2023-04-18 15:33:18
     * @return: top.dabaibai.websocket.listener.DistributedWebSocketListener
     * @version: 1.0
     */
    @Bean
    @ConditionalOnBean(DefaultWebSocketSender.class)
    public DistributedWebSocketListener distributedWebSocketListener() {
        return new DistributedWebSocketListener();
    }

    /**
     * @param context spring上下文
     * @description: 向WebSocket处理器注册中心注册自定义处理器与路径映射
     * @author: 白剑民
     * @date: 2023-06-05 23:58:50
     * @return: org.springframework.web.socket.config.annotation.WebSocketConfigurer
     * @version: 1.0
     */
    @Bean
    public WebSocketConfigurer registerCustomWebSocketHandlers(ApplicationContext context) {
        return new CustomWebSocketConfigurer(context);
    }
}
