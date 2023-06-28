package top.dabaibai.stream.consumer;

import top.dabaibai.stream.DbbMessage;

/**
 * @description: 消费者监听接口
 * @author: 白剑民
 * @dateTime: 2023-03-10 16:50:36
 */
@FunctionalInterface
public interface DbbConsumerListener {

    /**
     * @param message Stream消息实体
     * @description: 消息处理函数
     * @author: 白剑民
     * @date: 2023-03-10 16:26:59
     * @return: void
     * @version: 1.0
     */
    void onMessage(DbbMessage message);
}
