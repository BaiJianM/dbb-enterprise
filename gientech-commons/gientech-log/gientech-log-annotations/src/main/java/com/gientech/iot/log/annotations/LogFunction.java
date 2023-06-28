package com.gientech.iot.log.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 日志记录函数
 * @author: 王强
 * @dateTime: 2022-09-02 16:28:58
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LogFunction {

    /**
     * 自定义函数的别名，如果为空即使用函数名
     */
    String value() default "";
}
