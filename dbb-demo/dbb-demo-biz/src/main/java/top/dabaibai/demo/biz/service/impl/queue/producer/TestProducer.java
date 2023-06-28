package top.dabaibai.demo.biz.service.impl.queue.producer;

import com.alibaba.fastjson.JSON;
import top.dabaibai.demo.biz.entity.DoTest;
import top.dabaibai.stream.DbbMQConstant;
import top.dabaibai.stream.DbbMessage;
import top.dabaibai.stream.producer.DbbProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final DbbProducer dbbProducer;

    public void testStreamBridgeRocketMQ(String outputChannelName) {
        DoTest doTest = new DoTest();
        doTest.setName("测试RocketMQ消息发送");
        DbbMessage dbbMessage = DbbMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .build();
        try {
            dbbProducer.sendWithAsync(dbbMessage, () -> log.info("成功的回调"), () -> log.info("失败的回调"), outputChannelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testStreamBridgeRocketTransaction() {
        DoTest doTest = new DoTest();
        doTest.setName("测试StreamBridgeRocketmq事务消息发送");
        DbbMessage dbbMessage = DbbMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(DbbMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        doTest.setName("测试StreamBridgeRocketmq事务消息发送-1");
        DbbMessage dbbMessage2 = DbbMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(DbbMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        try {
            dbbProducer.sendWithAsync(dbbMessage, 3L, TimeUnit.SECONDS);
            dbbProducer.sendWithAsync(dbbMessage2, 10L, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(1);
            log.info("当前有 {} 任务正在排队", dbbProducer.getPendingMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
