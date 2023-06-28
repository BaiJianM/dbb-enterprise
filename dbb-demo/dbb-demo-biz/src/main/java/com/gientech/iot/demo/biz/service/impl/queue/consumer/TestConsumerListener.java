package com.gientech.iot.demo.biz.service.impl.queue.consumer;

import com.gientech.iot.stream.GientechMessage;
import com.gientech.iot.stream.consumer.GientechConsumerListener;
import com.gientech.iot.stream.consumer.GientechMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: spring-cloud-stream的消费者示例
 * @author: 白剑民
 * @dateTime: 2023/3/3 18:09
 */
@Slf4j
@GientechMQListener(tags = {"tag1"})
public class TestConsumerListener implements GientechConsumerListener {

    @Override
    public void onMessage(GientechMessage message) {
        log.info("接收到消息: {}", message);
    }
}
