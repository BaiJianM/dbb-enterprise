package top.dabaibai.stream.producer.delay;

import top.dabaibai.stream.producer.DbbProducer;

/**
 * @description: 自定义延时消息存储接口类
 * @author: 白剑民
 * @dateTime: 2023/3/30 13:38
 */
public interface DelayMessageStoreService {

    /**
     * @param state   消息发送状态
     * @param message 延时消息对象
     * @description: 更改延时消息对象的发送状态
     * @author: 白剑民
     * @date: 2023-03-30 14:12:59
     * @version: 1.0
     */
    void changeState(DelayMessageState state, DelayMessage message);

    /**
     * @param delayMessage 延时消息对象
     * @description: 延时消息存储
     * @author: 白剑民
     * @date: 2023-03-30 13:58:13
     * @version: 1.0
     */
    void store(DelayMessage delayMessage);

    /**
     * @param producer 生产者
     * @description: 系统每次启动时，执行消息重入延时逻辑
     * @author: 白剑民
     * @date: 2023-03-31 10:42:21
     * @version: 1.0
     */
    void initResend(DbbProducer producer);
}
