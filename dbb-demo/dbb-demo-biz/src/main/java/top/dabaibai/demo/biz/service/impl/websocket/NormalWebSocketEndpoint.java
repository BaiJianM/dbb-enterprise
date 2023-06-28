package top.dabaibai.demo.biz.service.impl.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 * @description: 普通的WebSocket服务实现
 * @author: 白剑民
 * @dateTime: 2023/6/8 23:21
 */
@Slf4j
@Component
@ServerEndpoint("/normal")
public class NormalWebSocketEndpoint {

    @OnOpen
    public void onOpen() {
        log.info("普通的WebSocket服务实现");
    }

}
