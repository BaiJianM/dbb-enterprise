package com.gientech.iot.database.annotation;

import java.lang.annotation.*;

/**
 * @description: 数据权限注解
 * @author: 白剑民
 * @dateTime: 2023/4/6 10:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 默认主表字段过滤(默认以企业id过滤)
     */
    String defaultField() default "enterprise_id";

    /**
     * 是否进行数据过滤
     */
    boolean isFilter() default true;

    /**
     * 忽略的表名
     */
    String[] ignoreTables() default {"sys_log"};
}
