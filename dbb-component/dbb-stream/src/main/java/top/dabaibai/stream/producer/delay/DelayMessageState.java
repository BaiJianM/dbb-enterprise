package top.dabaibai.stream.producer.delay;

/**
 * @description: 延时消息发送状态
 * @author: 白剑民
 * @dateTime: 2023/3/30 13:38
 */
public enum DelayMessageState {
    /**
     * 未发送
     */
    NOT_SEND,
    /**
     * 等待发送
     */
    SENDING,
    /**
     * 发送成功
     */
    SEND_SUCCESS,
    /**
     * 发送失败
     */
    SEND_FAIL,
    ;
}
