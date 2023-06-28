package com.gientech.iot.stream.consumer;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 消费者监听属性注解
 * @author: 王强
 * @dateTime: 2023-03-10 16:50:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface GientechMQListener {
    /**
     * @description: 消息标签集合(添加此属性表明消费者监听指定标签的消息)
     * @author: 王强
     * @date: 2023-03-10 23:47:45
     * @return: java.lang.String[]
     * @version: 1.0
     */
    String[] tags() default "";
}
