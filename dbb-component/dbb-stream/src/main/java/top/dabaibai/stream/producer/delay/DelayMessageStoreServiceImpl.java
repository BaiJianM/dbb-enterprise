package top.dabaibai.stream.producer.delay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import top.dabaibai.stream.producer.DbbProducer;
import top.dabaibai.thread.pool.DbbThreadPool;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 延时消息因使用系统内存进行存储，考虑服务宕机或重启的情况
 * 需要对未发送和发送失败的消息进行持久化存储，默认使用实体文件(DelayMessage.json)实现
 * @author: 白剑民
 * @dateTime: 2023/3/30 14:10
 */
@Slf4j
public class DelayMessageStoreServiceImpl implements DelayMessageStoreService {

    /**
     * 延时消息列表存储文件名
     */
    private static final String STORE_FILENAME = "DelayMessage.json";
    /**
     * 文件存储路径
     */
    private static final String STORE_PATH = System.getProperty("user.dir") + File.separator + STORE_FILENAME;

    /**
     * 本地缓存延时消息列表，待写入文件
     */
    private List<DelayMessage> storeList = new ArrayList<>();

    @Override
    public void changeState(DelayMessageState state, DelayMessage delayMessage) {
        synchronized (this) {
            // 如果消息发送状态为成功发送，则将其移出延时消息列表并更新文件
            if (state == DelayMessageState.SEND_SUCCESS) {
                // 移除
                storeList.remove(delayMessage);
                // 重新写入
                writeToFile();
            }
        }
    }

    @Override
    public void store(DelayMessage delayMessage) {
        // 写入消息缓存列表
        storeList.add(delayMessage);
        // 写入文件
        writeToFile();
    }

    @Override
    public void initResend(DbbProducer producer) {
        // 读取延时消息缓存文件
        List<DelayMessage> delayMessages = this.readFile();
        if (delayMessages != null && delayMessages.size() > 0) {
            this.storeList = delayMessages;
            log.info("即将开始执行延时消息重新发送");
            // 取出未发送的延时消息
            List<DelayMessage> notSendList = delayMessages.stream()
                    .filter(message -> message.getState() == DelayMessageState.NOT_SEND)
                    .collect(Collectors.toList());
            log.info("待处理延时消息列表大小为: {}", notSendList.size());
            int size = notSendList.size();
            CountDownLatch count = new CountDownLatch(size);
            notSendList.forEach(delayMessage -> {
                DbbThreadPool.init().execute(() -> {
                    // 重新计算延时时间
                    long timeout = delayMessage.getTimeout() - System.currentTimeMillis();
                    // 如果小于等于0则立即执行消息发送
                    if (timeout <= 0) {
                        producer.sendWithSync(delayMessage.getMessage());
                    } else {
                        log.info("======>>>>>>延时消息将于 {} 重新发送", delayMessage.timeFormat());
                        // 否则重入时间轮进行延时等待
                        DbbProducer.TIMER.newTimeout(t -> {
                            DbbProducer.MessageSendResult result = producer.sendWithSync(delayMessage.getMessage());
                            if (result.getSendResult()) {
                                this.changeState(DelayMessageState.SEND_SUCCESS, delayMessage);
                            }
                        }, timeout, TimeUnit.MILLISECONDS);
                    }
                    count.countDown();
                });
                try {
                    count.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
            log.warn("延时消息缓存文件不存在或内容为空，将跳过处理未发送的延时消息");
        }
    }

    /**
     * @description: 读取延时消息文件
     * @author: 白剑民
     * @date: 2023-03-30 19:23:43
     * @return: java.util.List<top.dabaibai.stream.producer.delay.DelayMessage>
     * @version: 1.0
     */
    public List<DelayMessage> readFile() {
        List<DelayMessage> delayMessages = new ArrayList<>();
        // 读取文件内容
        File file = new File(STORE_PATH);
        if (!file.exists()) {
            return delayMessages;
        }
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(STORE_PATH));
            delayMessages = JSONArray.parseArray(new String(bytes, StandardCharsets.UTF_8), DelayMessage.class);
        } catch (Exception e) {
            log.error("读取延时消息缓存文件出错，错误信息: {}", e.getMessage());
        }
        return delayMessages;
    }

    /**
     * @description: 写入延时消息存储文件
     * @author: 白剑民
     * @date: 2023-03-30 15:12:05
     * @version: 1.0
     */
    public void writeToFile() {
        // 判断是否已存在DelayMessage.json文件，文件默认生成至项目根目录
        File file = new File(STORE_PATH);
        try {
            if (!file.exists()) {
                // 不存在则生成
                file.createNewFile();
            }
            // 操作文件写入(全量)
            BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
            bufferedWriter.write(JSON.toJSONString(storeList, SerializerFeature.PrettyFormat));
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("延时消息存储出错，错误原因: {}", e.getMessage());
        }
    }
}
