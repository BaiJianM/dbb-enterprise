package top.dabaibai.demo.biz.service.impl.redislistener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import top.dabaibai.redis.annotation.RedisChannel;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/17 15:01
 */
@Slf4j
@Component
@RedisChannel(name = "777")
public class RedisMessageBroadcastListenerA implements MessageListener {
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        String msg = JSON.parseObject(new String(message.getBody(), StandardCharsets.UTF_8), String.class);
        String channel = new String(message.getChannel());
        log.info("广播监听消息: {}, 频道: {}", msg, channel);
    }
}
