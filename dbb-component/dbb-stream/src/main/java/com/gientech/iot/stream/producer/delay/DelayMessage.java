package com.gientech.iot.stream.producer.delay;

import com.gientech.iot.stream.GientechMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @description: 延时消息对象
 * @author: 白剑民
 * @dateTime: 2023/3/30 13:38
 */
@Data
@AllArgsConstructor
public class DelayMessage implements Serializable {

    private static final long serialVersionUID = -5810021112492820409L;

    /**
     * 消息体
     */
    private GientechMessage message;
    /**
     * 消息发送状态
     */
    private DelayMessageState state;
    /**
     * 到期执行时间（单位：毫秒）
     */
    private Long timeout;

    /**
     * @description: 返回到期执行时间的格式化结果
     * @author: 白剑民
     * @date: 2023-03-31 09:47:37
     * @return: java.lang.String
     * @version: 1.0
     */
    public String timeFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeout), ZoneId.of("+8"));
        return localDateTime.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelayMessage that = (DelayMessage) o;
        return message.equals(that.message) && timeout.equals(that.timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, timeout);
    }
}
