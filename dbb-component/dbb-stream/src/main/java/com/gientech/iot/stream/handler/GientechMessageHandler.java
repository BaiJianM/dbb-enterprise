package com.gientech.iot.stream.handler;

import com.gientech.iot.stream.GientechMQConstant;
import com.gientech.iot.stream.GientechMessage;
import com.gientech.iot.stream.consumer.ConsumerInitializer;
import com.gientech.iot.stream.consumer.GientechConsumerListener;
import com.gientech.iot.stream.consumer.GientechMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @description: 自定义消息处理器
 * @author: 白剑民
 * @dateTime: 2023/3/11 12:42
 */
@Slf4j
public final class GientechMessageHandler {

    private final ConsumerInitializer consumers;

    public GientechMessageHandler(ConsumerInitializer consumers) {
        this.consumers = consumers;
    }

    /**
     * @param message spring-cloud-stream消息体
     * @description: 消息处理
     * @author: 白剑民
     * @date: 2023-03-11 12:42:14
     * @version: 1.0
     */
    public void handleMessage(Message<GientechMessage> message) {
        // 遍历所有消费者监听类
        Set<Map.Entry<String, GientechConsumerListener>> entries = consumers.getConsumerListeners().entrySet();
        Set<String> tags = consumers.getTags();
        for (Map.Entry<String, GientechConsumerListener> entry : entries) {
            // 得到消费者监听的实现类
            GientechConsumerListener consumer = entry.getValue();
            // 获取其上标注的消费者属性注解
            GientechMQListener annotation = consumer.getClass().getAnnotation(GientechMQListener.class);
            // 获取注解中的标签属性
            Object msgTag = message.getHeaders().get(GientechMQConstant.Headers.TAGS);
            // 如果是RocketMQ的消息头，TAGS名称会被转换成ROCKET_TAGS
            if (msgTag == null) {
                // 再尝试以RocketMQ转换过的消息头获取TAGS
                msgTag = message.getHeaders().get("ROCKET_" + GientechMQConstant.Headers.TAGS);
            }
            assert msgTag != null;
            boolean contains = tags.contains(msgTag.toString());
            if (contains) {
                // 将spring-cloud-stream传入的标签分发给各个消费者监听类并执行它们的处理方法
                if (Arrays.asList(annotation.tags()).contains(String.valueOf(msgTag))) {
                    consumer.onMessage(message.getPayload());
                }
            } else {
                log.error("未知的tag值");
            }
        }
    }
}
