package com.gientech.iot.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: MyBatisPlus自定义悲观锁拦截注解
 * @author: 白剑民
 * @dateTime: 2023-03-02 12:01:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PessimisticLockInterceptor {

    /**
     * @description: true: 执行select语句时，会被ForUpdateInterceptor拦截，在语句后添加 "for update"
     * @author: 白剑民
     * @date: 2023-03-02 13:51:10
     * @return: boolean
     * @version: 1.0
     */
    boolean forUpdate() default false;
}
