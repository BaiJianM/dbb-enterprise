package top.dabaibai.log.annotations;

import java.lang.annotation.*;

/**
 * @description: 操作日志
 * @author: 白剑民
 * @dateTime: 2022-09-02 16:29:09
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogs {

    OperationLog[] value();
}
