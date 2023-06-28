package top.dabaibai.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 广播模式监听频道
 * @author: 白剑民
 * @dateTime: 2023/4/17 11:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RedisChannel {
    /**
     * @description: 频道名称
     * @author: 白剑民
     * @date: 2023-04-17 14:33:58
     * @return: java.lang.String
     * @version: 1.0
     */
    String name();
}
