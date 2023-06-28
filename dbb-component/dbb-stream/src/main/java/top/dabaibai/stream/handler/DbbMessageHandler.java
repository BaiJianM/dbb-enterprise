package top.dabaibai.stream.handler;

import top.dabaibai.stream.DbbMessage;
import top.dabaibai.stream.DbbMQConstant;
import top.dabaibai.stream.consumer.ConsumerInitializer;
import top.dabaibai.stream.consumer.DbbConsumerListener;
import top.dabaibai.stream.consumer.DbbMQListener;
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
public final class DbbMessageHandler {

    private final ConsumerInitializer consumers;

    public DbbMessageHandler(ConsumerInitializer consumers) {
        this.consumers = consumers;
    }

    /**
     * @param message spring-cloud-stream消息体
     * @description: 消息处理
     * @author: 白剑民
     * @date: 2023-03-11 12:42:14
     * @version: 1.0
     */
    public void handleMessage(Message<DbbMessage> message) {
        // 遍历所有消费者监听类
        Set<Map.Entry<String, DbbConsumerListener>> entries = consumers.getConsumerListeners().entrySet();
        Set<String> tags = consumers.getTags();
        for (Map.Entry<String, DbbConsumerListener> entry : entries) {
            // 得到消费者监听的实现类
            DbbConsumerListener consumer = entry.getValue();
            // 获取其上标注的消费者属性注解
            DbbMQListener annotation = consumer.getClass().getAnnotation(DbbMQListener.class);
            // 获取注解中的标签属性
            Object msgTag = message.getHeaders().get(DbbMQConstant.Headers.TAGS);
            // 如果是RocketMQ的消息头，TAGS名称会被转换成ROCKET_TAGS
            if (msgTag == null) {
                // 再尝试以RocketMQ转换过的消息头获取TAGS
                msgTag = message.getHeaders().get("ROCKET_" + DbbMQConstant.Headers.TAGS);
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
