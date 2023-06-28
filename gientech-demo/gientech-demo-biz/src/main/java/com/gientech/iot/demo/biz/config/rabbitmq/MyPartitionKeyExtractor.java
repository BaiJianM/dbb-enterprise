package com.gientech.iot.demo.biz.config.rabbitmq;

import com.gientech.iot.stream.GientechMQConstant;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义分区键提取器（获取分区标识）
 * @author: 王强
 * @dateTime: 2023-03-10 12:47:00
 */
@Component
public class MyPartitionKeyExtractor implements PartitionKeyExtractorStrategy {
    @Override
    public Object extractKey(Message<?> message) {
        return message.getHeaders().get(GientechMQConstant.Headers.TAGS);
    }
}
