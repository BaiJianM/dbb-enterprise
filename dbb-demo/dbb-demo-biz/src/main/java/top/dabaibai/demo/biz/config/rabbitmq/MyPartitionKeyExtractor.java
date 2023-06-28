package top.dabaibai.demo.biz.config.rabbitmq;

import top.dabaibai.stream.DbbMQConstant;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义分区键提取器（获取分区标识）
 * @author: 白剑民
 * @dateTime: 2023-03-10 12:47:00
 */
@Component
public class MyPartitionKeyExtractor implements PartitionKeyExtractorStrategy {
    @Override
    public Object extractKey(Message<?> message) {
        return message.getHeaders().get(DbbMQConstant.Headers.TAGS);
    }
}
