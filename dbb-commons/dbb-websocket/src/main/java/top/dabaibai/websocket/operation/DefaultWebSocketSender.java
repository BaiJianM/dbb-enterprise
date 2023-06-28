package top.dabaibai.websocket.operation;

import com.alibaba.fastjson.JSON;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.websocket.server.WebSocketSessionCache;
import top.dabaibai.websocket.vo.WebSocketMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static top.dabaibai.websocket.handler.DbbWebSocketHandler.WEBSOCKET_CHANNEL;

/**
 * @description: 默认的分布式websocket消息发送实现类
 * @author: 白剑民
 * @dateTime: 2023/4/17 17:06
 */
@Slf4j
public class DefaultWebSocketSender extends AbstractWebSocketSender {

    private final RedisUtils redisUtils;

    public DefaultWebSocketSender(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean sendMessageByDistributed(WebSocketMessageVO message) {
        boolean sendResult = false;
        // 判断当前服务节点有无Session缓存
        WebSocketSession session = WebSocketSessionCache.get(message.getSocketType(), message.getSocketId());
        if (session == null) {
            // 如果没有，则尝试发布redis广播通知，让其他节点进行本地session检查并发送消息
            sendResult = redisUtils.convertAndSend(WEBSOCKET_CHANNEL, JSON.toJSONString(message)).orElse(false);
        } else {
            // 如果有，则直接发送消息
            synchronized (this) {
                try {
                    TextMessage textMessage = new TextMessage(message.getContent());
                    session.sendMessage(textMessage);
                    sendResult = true;
                } catch (IOException e) {
                    log.error("多节点分布式WebSocket消息发送出错，错误信息: {}", e.getMessage());
                }
            }
        }
        return sendResult;
    }
}
