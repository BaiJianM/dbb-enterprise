package com.gientech.iot.stream;

import com.alibaba.cloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import com.gientech.iot.stream.consumer.ConsumerInitializer;
import com.gientech.iot.stream.handler.GientechMessageHandler;
import com.gientech.iot.stream.producer.GientechProducer;
import com.gientech.iot.stream.producer.delay.DelayMessageStoreService;
import com.gientech.iot.stream.producer.delay.DelayMessageStoreServiceImpl;
import com.gientech.iot.stream.producer.delay.StoreDelayMessageResend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * @description: 自定义生产者消费者自动装配类
 * @author: 白剑民
 * @dateTime: 2023/3/10 14:45
 */
@Slf4j
@Configuration
@Import({StoreDelayMessageResend.class})
public class GientechStreamAutoConfiguration {

    /**
     * @param handler 消息处理器
     * @description: 构造默认的消费者监听
     * @author: 白剑民
     * @date: 2023-03-10 14:47:24
     * @return: java.util.function.Consumer<org.springframework.messaging.Message < ?>>
     * @version: 1.0
     */
    @Bean(GientechProducer.DEFAULT_DEFINITION)
    public Consumer<Message<GientechMessage>> gientechConsumer(GientechMessageHandler handler) {
        return handler::handleMessage;
    }

    /**
     * @param initializer 消费者初始化
     * @description: 自定义消息处理器
     * @author: 白剑民
     * @date: 2023-05-08 23:09:23
     * @return: com.gientech.iot.stream.handler.GientechMessageHandler
     * @version: 1.0
     */
    @Bean
    public GientechMessageHandler messageHandler(ConsumerInitializer initializer) {
        return new GientechMessageHandler(initializer);
    }

    /**
     * @param context    spring上下文
     * @description: 消费者初始化
     * @author: 白剑民
     * @date: 2023-05-08 23:12:57
     * @return: com.gientech.iot.stream.consumer.ConsumerInitializer
     * @version: 1.0
     */
    @Bean
    public ConsumerInitializer consumerInitializer(ApplicationContext context) {
        return new ConsumerInitializer(context);
    }

    /**
     * @param bridge                   负责消息发送的对象
     * @param bindingServiceProperties spring-cloud-stream的bindings配置信息
     * @param delayMessageStoreService 延时消息处理
     * @description: 构造一个生产者
     * @author: 白剑民
     * @date: 2023-03-11 00:23:59
     * @return: com.gientech.iot.stream.producer.GientechProducer
     * @version: 1.0
     */
    @Bean
    public GientechProducer gientechProducer(StreamBridge bridge, BindingServiceProperties bindingServiceProperties,
                                             DelayMessageStoreService delayMessageStoreService) {
        return new GientechProducer(bridge, bindingServiceProperties, delayMessageStoreService);
    }

    /**
     * @description: 延时消息持久化处理器
     * @author: 白剑民
     * @date: 2023-03-30 17:39:22
     * @return: com.gientech.iot.stream.producer.delay.DelayMessageStoreService
     * @version: 1.0
     */
    @Bean
    @ConditionalOnMissingBean(DelayMessageStoreService.class)
    public DelayMessageStoreService delayMessageStoreService() {
        log.warn("默认提供的延时消息持久化处理器会将数据存储于本地文件当中，存在多服务节点下消息丢失的问题，详见README.md文件");
        return new DelayMessageStoreServiceImpl();
    }
}
