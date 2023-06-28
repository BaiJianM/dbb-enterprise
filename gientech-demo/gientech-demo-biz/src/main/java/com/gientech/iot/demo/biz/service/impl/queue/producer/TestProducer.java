package com.gientech.iot.demo.biz.service.impl.queue.producer;

import com.alibaba.cloud.stream.binder.rocketmq.constant.RocketMQConst;
import com.alibaba.fastjson.JSON;
import com.gientech.iot.demo.biz.entity.DoTest;
import com.gientech.iot.stream.GientechMQConstant;
import com.gientech.iot.stream.GientechMessage;
import com.gientech.iot.stream.producer.GientechProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description: spring-cloud-stream的生产者示例
 * @author: 白剑民
 * @dateTime: 2023/3/3 18:16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TestProducer {

    private final GientechProducer gientechProducer;

    public void testStreamBridgeRocketMQ(String outputChannelName) {
        DoTest doTest = new DoTest();
        doTest.setName("测试RocketMQ消息发送");
        GientechMessage gientechMessage = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .build();
        try {
            gientechProducer.sendWithAsync(gientechMessage, () -> log.info("成功的回调"), () -> log.info("失败的回调"), outputChannelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testStreamBridgeRocketTransaction() {
        DoTest doTest = new DoTest();
        doTest.setName("测试StreamBridgeRocketmq事务消息发送");
        GientechMessage gientechMessage = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(GientechMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        doTest.setName("测试StreamBridgeRocketmq事务消息发送-1");
        GientechMessage gientechMessage2 = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(GientechMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        try {
            gientechProducer.sendWithAsync(gientechMessage, 3L, TimeUnit.SECONDS);
            gientechProducer.sendWithAsync(gientechMessage2, 10L, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(1);
            log.info("当前有 {} 任务正在排队", gientechProducer.getPendingMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
