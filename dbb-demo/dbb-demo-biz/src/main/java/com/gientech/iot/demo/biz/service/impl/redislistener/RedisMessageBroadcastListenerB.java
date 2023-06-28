package com.gientech.iot.demo.biz.service.impl.redislistener;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.redis.annotation.RedisChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @description: Redis广播模式监听器
 * @author: 白剑民
 * @dateTime: 2023/4/17 13:37
 */
@Slf4j
@Component
@RedisChannel(name = "666")
public class RedisMessageBroadcastListenerB implements MessageListener {
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        String msg = JSON.parseObject(new String(message.getBody(), StandardCharsets.UTF_8), String.class);
        String channel = new String(message.getChannel());
        log.info("广播监听消息: {}, 频道: {}", msg, channel);
    }
}
