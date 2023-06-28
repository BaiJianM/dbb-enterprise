package top.dabaibai.stream.producer;

/**
 * @description: 通用消息发送回调
 * @author: 白剑民
 * @dateTime: 2023/3/11 14:05
 */
@FunctionalInterface
public interface SendCallback {
    void execute();
}
