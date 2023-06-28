package top.dabaibai.websocket.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @description: WebSocket消息体
 * @author: 白剑民
 * @dateTime: 2023/4/18 16:54
 */
@Data
@Builder
public class WebSocketMessageVO {
    /**
     * socket连接类型
     */
    private String socketType;
    /**
     * socket连接唯一标识
     */
    private String socketId;
    /**
     * 消息内容
     */
    private String content;
}
