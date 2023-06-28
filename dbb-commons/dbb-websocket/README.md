## Gientech-WebSocket 模块

#### 模块简介

```
此模块为WebSocket增强实现依赖，提供简单增强注解(目的是为了限制业务开发使用)使用和集成本部门常见开发业务的拦截器以及分布式session的redis解决方案
```

#### 包路径说明

- com.gientech.iot.websocket.annotations：服务端点注解
- com.gientech.iot.websocket.handler：请求处理器
- com.gientech.iot.websocket.interceptor：握手拦截器
- com.gientech.iot.websocket.listener：分布式session实现，通过集成Redis订阅频道实现节点本地缓存发现
- com.gientech.iot.websocket.operation：服务端主动消息发送实现
- com.gientech.iot.websocket.server：服务端配置
- com.gientech.iot.websocket.vo：消息对象
- com.gientech.iot.websocket：模块自动配置

#### 模块功能示例

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-websocket</artifactId>
    <version>1.0.0</version>
</dependency>
```

1、使用原注解方式

```java
// 本模块只是对websocket做了增强，原注解方式仍然可正常使用
@Component
@ServerEndpoint("/normal")
public class NormalWebSocketEndpoint {

    @OnOpen
    public void onOpen() {
        log.info("普通的WebSocket服务实现");
    }

}
```

2、使用简单增强注解

```java
// 实现GientechWebSocketHandler接口，并标注@GientechWebSocketEndpoint注解
// 该注解可接收两个参数：监听路径和WebSocket握手时的拦截器列表，前者为必传，后者为选传
// 拦截器列表可通过继承AbstractCustomHandshakeInterceptor并重写beforeHandshakeEx方法实现，该方法返回一个布尔值来表示握手成功还是失败
// 同时继承AbstractCustomHandshakeInterceptor的类必须是一个有效的Spring Bean
@GientechWebSocketEndpoint(value = "/test",
        handshakeInterceptors = {DemoWebSocketInterceptor1.class, DemoWebSocketInterceptor2.class})
public class DemoWebSocketHandler implements GientechWebSocketHandler {
    @Override
    public void onOpen(String socketType, String socketId, WebSocketSession session) {
        log.info("连接时的逻辑");
    }

    @Override
    public void onClose(String socketType, String socketId, WebSocketSession session) {
        log.info("关闭连接时的逻辑");
    }

    @Override
    public void onMessage(String socketType, String socketId, String message, WebSocketSession session) {
        log.info("收到消息时的逻辑");
    }

    @Override
    public void onError(String socketType, String socketId, WebSocketSession session, Throwable error) {
        log.info("连接出错时的逻辑");
    }
}
```