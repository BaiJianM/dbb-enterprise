package top.dabaibai.demo.biz.service.impl.queue.consumer;

import top.dabaibai.stream.DbbMessage;
import top.dabaibai.stream.consumer.DbbConsumerListener;
import top.dabaibai.stream.consumer.DbbMQListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: spring-cloud-stream的消费者示例
 * @author: 白剑民
 * @dateTime: 2023/3/3 18:09
 */
@Slf4j
@DbbMQListener(tags = {"tag1"})
public class TestConsumerListener implements DbbConsumerListener {

    @Override
    public void onMessage(DbbMessage message) {
        log.info("接收到消息: {}", message);
    }
}
