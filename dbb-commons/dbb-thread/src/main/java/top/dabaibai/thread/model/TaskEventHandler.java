package top.dabaibai.thread.model;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/7/30 21:16
 */
@Slf4j
public class TaskEventHandler implements EventHandler<Task> {
    @Override
    public void onEvent(Task event, long sequence, boolean endOfBatch) throws Exception {
        try {
            TimeUnit.SECONDS.sleep(1);
            log.info("消息消费开始");
            if (event != null) {
                log.info("接收到消息: {}", JSON.toJSONString(event));
            }
        } catch (Exception e) {
            log.error("错误信息: {}", e.getMessage());
        }
    }
}
