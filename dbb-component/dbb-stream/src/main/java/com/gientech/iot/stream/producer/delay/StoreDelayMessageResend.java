package com.gientech.iot.stream.producer.delay;

import com.gientech.iot.stream.producer.GientechProducer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @description: 因系统关闭重启时导致存储于内存中的的延时消息丢失，重入延时
 * @author: 白剑民
 * @dateTime: 2023/3/31 13:48
 */
@Slf4j
public class StoreDelayMessageResend {

    private final GientechProducer producer;

    private final DelayMessageStoreService storeService;

    public StoreDelayMessageResend(GientechProducer producer, DelayMessageStoreService storeService) {
        this.producer = producer;
        this.storeService = storeService;
    }

    /**
     * @description: 每一次服务启动时，都检查并处理未发送的延时消息
     * @author: 白剑民
     * @date: 2023-03-30 21:37:48
     * @version: 1.0
     */
    @PostConstruct
    public void execute() {
        // 执行其中的消息重入延时方法
        storeService.initResend(producer);
    }
}
