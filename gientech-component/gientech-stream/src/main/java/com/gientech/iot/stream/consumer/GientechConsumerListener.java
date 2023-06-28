package com.gientech.iot.stream.consumer;

import com.gientech.iot.stream.GientechMessage;

/**
 * @description: 消费者监听接口
 * @author: 王强
 * @dateTime: 2023-03-10 16:50:36
 */
@FunctionalInterface
public interface GientechConsumerListener {

    /**
     * @param message Stream消息实体
     * @description: 消息处理函数
     * @author: 王强
     * @date: 2023-03-10 16:26:59
     * @return: void
     * @version: 1.0
     */
    void onMessage(GientechMessage message);
}
