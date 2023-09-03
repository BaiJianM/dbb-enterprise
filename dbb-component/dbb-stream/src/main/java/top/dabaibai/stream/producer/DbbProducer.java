package top.dabaibai.stream.producer;

import io.netty.util.HashedWheelTimer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.lang.Nullable;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import top.dabaibai.stream.DbbMQConstant;
import top.dabaibai.stream.DbbMessage;
import top.dabaibai.stream.producer.delay.DelayMessage;
import top.dabaibai.stream.producer.delay.DelayMessageState;
import top.dabaibai.stream.producer.delay.DelayMessageStoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @description: 生产者
 * @author: 白剑民
 * @dateTime: 2023/3/10 15:01
 */
@Slf4j
public class DbbProducer {

    /**
     * 默认的消费函数名(Bean名，对应配置文件中的spring.cloud.function.definition)
     */
    public static final String DEFAULT_DEFINITION = "dbb";

    private final StreamBridge bridge;

    private final BindingServiceProperties bindingServiceProperties;

    private final DelayMessageStoreService delayMessageStoreService;

    /**
     * 定义一个netty的时间轮算法实现类，用于实现延时消息发送
     * 发送的消息一旦被设置为延时消息，则先经过该类进行延时存储，到期后由该类进行统一发送
     */
    public static final HashedWheelTimer TIMER = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);

    public DbbProducer(StreamBridge bridge, BindingServiceProperties bindingServiceProperties,
                       DelayMessageStoreService delayMessageStoreService) {
        this.bridge = bridge;
        this.bindingServiceProperties = bindingServiceProperties;
        this.delayMessageStoreService = delayMessageStoreService;
    }

    /**
     * @param message 自定义消息
     * @description: 同步消息发送
     * 1.当前环境配置中只存在一种消息output通道时, 无需进行传参, 将默认读取
     * 2.当存在多个output通道时将抛出异常
     * @author: 白剑民
     * @date: 2023-03-11 19:39:20
     * @version: 1.0
     */
    public MessageSendResult sendWithSync(DbbMessage message) throws MultipleOutputChannelPropertyException {
        return this.send(message, getSingleOutputChannel()).get(0);
    }

    /**
     * @param message  自定义消息
     * @param channels spring.cloud.stream.bindings.<channelName> 中配置的output消息通道
     * @description: 同步消息发送
     * @author: 白剑民
     * @date: 2023-03-11 19:39:20
     * @version: 1.0
     */
    public List<MessageSendResult> sendWithSync(DbbMessage message, String... channels) {
        return this.send(message, channels);
    }

    /**
     * @param message 自定义消息
     * @description: 异步消息发送
     * @author: 白剑民
     * @date: 2023-03-11 19:39:20
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message) throws MultipleOutputChannelPropertyException {
        this.send(message, getSingleOutputChannel());
    }

    /**
     * @param message 自定义消息
     * @param delay   延时发送时间
     * @param unit    延时时间单位
     * @description: 异步延时消息发送
     * @author: 白剑民
     * @date: 2023-03-11 19:39:20
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message, Long delay, TimeUnit unit) throws MultipleOutputChannelPropertyException {
        this.sendDelayMessage(() -> send(message, getSingleOutputChannel()), message, delay, unit);
    }

    /**
     * @param message  自定义消息
     * @param channels spring.cloud.stream.bindings.<channelName> 中配置的output消息通道
     * @description: 异步消息发送
     * @author: 白剑民
     * @date: 2023-03-11 13:52:49
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message, String... channels) {
        this.send(message, channels);
    }

    /**
     * @param message  自定义消息
     * @param delay    延时发送时间
     * @param unit     延时时间单位
     * @param channels spring.cloud.stream.bindings.<channelName> 中配置的output消息通道
     * @description: 异步延时消息发送
     * @author: 白剑民
     * @date: 2023-03-11 19:39:20
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message, Long delay, TimeUnit unit, String... channels) throws MultipleOutputChannelPropertyException {
        this.sendDelayMessage(() -> send(message, channels), message, delay, unit);
    }

    /**
     * @param message   自定义消息
     * @param onSuccess 发送成功时的回调
     * @param onFail    发送失败时的回调
     * @description: 异步消息发送（可带回调函数）
     * @author: 白剑民
     * @date: 2023-03-12 09:47:27
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message, @Nullable SendCallback onSuccess, @Nullable SendCallback onFail) {
        // 调用消息发送方法并获取发送结果集合
        List<MessageSendResult> sendResults = this.send(message, getSingleOutputChannel());
        // 对结果进行遍历
        sendResults.forEach(res -> {
            // 执行发送成功回调方法，执行成功后跳过其后的失败方法逻辑，继续遍历
            if (res.sendResult && onSuccess != null) {
                onSuccess.execute();
                return;
            }
            if (!res.sendResult && onFail != null) {
                onFail.execute();
            }
        });
    }

    /**
     * @param message   自定义消息
     * @param onSuccess 发送成功时的回调
     * @param onFail    发送失败时的回调
     * @param channels  spring.cloud.stream.bindings.<channelName> 中配置的output消息通道
     * @description: 异步消息发送（可带回调函数）
     * @author: 白剑民
     * @date: 2023-03-11 13:52:49
     * @version: 1.0
     */
    @Async("asyncServiceExecutor")
    public void sendWithAsync(DbbMessage message, @Nullable SendCallback onSuccess, @Nullable SendCallback onFail,
                              String... channels) {
        // 调用消息发送方法并获取发送结果集合
        List<MessageSendResult> sendResults = this.send(message, channels);
        // 对结果进行遍历
        sendResults.forEach(res -> {
            try {
                // 执行发送成功回调方法，执行成功后跳过其后的失败方法逻辑，继续遍历
                if (res.sendResult && onSuccess != null) {
                    onSuccess.execute();
                    return;
                }
                if (!res.sendResult && onFail != null) {
                    onFail.execute();
                }
            } catch (Exception e) {
                log.error("异步消息发送回调执行错误，错误信息: {}", e.getMessage());
            }
        });
    }

    /**
     * @param message  自定义消息
     * @param channels spring.cloud.stream.bindings.<channelName> 中配置的output消息通道
     * @description: 消息发送
     * @author: 白剑民
     * @date: 2023-03-11 13:52:53
     * @return: java.util.List<top.dabaibai.stream.producer.DbbProducer.MessageSendResult>
     * @version: 1.0
     */
    private List<MessageSendResult> send(DbbMessage message, String... channels) {
        List<MessageSendResult> resultList = new ArrayList<>();
        MessageBuilder<DbbMessage> msgBuilder = MessageBuilder.withPayload(message);
        message.getHeaders().forEach(msgBuilder::setHeader);
        for (String channel : channels) {
            try {
                boolean send = bridge.send(channel, msgBuilder.build());
                resultList.add(new MessageSendResult(channel, send));
            } catch (Exception e) {
                log.error("生产者消息发送失败: {}", e.getMessage());
                resultList.add(new MessageSendResult(channel, false));
            }
        }
        return resultList;
    }

    /**
     * @description: 获取单独配置一个的消息输出通道名
     * @author: 白剑民
     * @date: 2023-03-12 09:45:28
     * @return: java.lang.String
     * @version: 1.0
     */
    private String getSingleOutputChannel() {
        // 获取所有的spring.cloud.stream.bindings配置，并取其key集合
        Set<String> channelNames = this.bindingServiceProperties.getBindings().keySet();
        // 获取输出通道名
        List<String> outputChannels = channelNames.stream()
                .filter(s -> s.contains(DbbMQConstant.Property.OUTPUT_CHANNEL_SUFFIX))
                .collect(Collectors.toList());
        // 判断是否存在多个输出通道
        if (outputChannels.size() > 1) {
            throw new MultipleOutputChannelPropertyException(
                    "存在多个output配置，使用默认通道进行消息发送时，只允许存在一个output，" +
                            "请检查通道配置，详情可参照/resources/stream-queue-config-example中的配置示例");
        }
        return outputChannels.get(0);
    }

    /**
     * @param task    要执行的延时任务
     * @param message 自定义消息
     * @param delay   延时执行时间
     * @param unit    延时时间单位
     * @description: 执行延时消息发送
     * @author: 白剑民
     * @date: 2023-03-29 22:01:58
     * @version: 1.0
     */
    private void sendDelayMessage(Supplier<List<MessageSendResult>> task,
                                  DbbMessage message, Long delay, TimeUnit unit) {
        // 如果设置了延时发送时间，且大于0，则将消息发送动作交给netty时间轮
        if (delay != null && delay > 0) {
            if (unit == null) {
                throw new IllegalArgumentException("非法的TimeUnit参数，当传入延时发送时间时，该参数不可为null");
            }
            // 计算到期时间
            long timeout = unit.toMillis(delay) + System.currentTimeMillis();
            DelayMessage delayMessage = new DelayMessage(message, DelayMessageState.NOT_SEND, timeout);
            delayMessageStoreService.store(delayMessage);
            TIMER.newTimeout((t -> {
                // TODO 多通道的判断较为复杂，暂且认为只要有一个发送结果是成功的，就是认为是成功发送
                List<MessageSendResult> sendResults = task.get();
                AtomicReference<DelayMessageState> state = new AtomicReference<>(DelayMessageState.SEND_FAIL);
                sendResults.stream()
                        .filter(MessageSendResult::getSendResult)
                        .findFirst()
                        .ifPresent(m -> state.set(DelayMessageState.SEND_SUCCESS));
                delayMessageStoreService.changeState(state.get(), delayMessage);
            }), delay, unit);
        } else {
            task.get();
        }
    }

    /**
     * @description: 获取正在等待发送的延时消息数量
     * @author: 白剑民
     * @date: 2023-03-29 22:10:12
     * @return: long
     * @version: 1.0
     */
    public long getPendingMessage() {
        return TIMER.pendingTimeouts();
    }

    /**
     * @description: 发送结果
     * @author: 白剑民
     * @dateTime: 2023/3/10 15:01
     */
    @Data
    @AllArgsConstructor
    public static class MessageSendResult {
        /**
         * 输出通道名
         */
        private String channelName;
        /**
         * 消息发送结果
         */
        private Boolean sendResult;
    }
}
