package top.dabaibai.web.configuration.repeat;

import java.lang.annotation.*;

/**
 * @description: 校验重复请求注解
 * @author: 白剑民
 * @dateTime: 2022-10-29 21:09:33
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatRequest {

    /**
     * @description: 幂等参数缓存时间（秒）
     * @author: 白剑民
     * @date: 2023-04-26 10:49:11
     * @return: long
     * @version: 1.0
     */
    long expired() default 3;

    /**
     * @description: 消息提示
     * @author: 白剑民
     * @date: 2023-04-26 10:49:13
     * @return: String
     * @version: 1.0
     */
    String message() default "操作过于频繁, 请稍后再试!";
}
