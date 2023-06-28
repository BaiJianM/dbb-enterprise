package com.gientech.iot.log.core.pojo;

import lombok.Data;

/**
 * @description: 发生变化的字段信息
 * @author: 白剑民
 * @dateTime: 2022-09-01 17:57:19
 */
@Data
public class DiffFieldVO {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段别名
     */
    private String fieldAlias;

    /**
     * 旧值
     */
    private Object oldValue;

    /**
     * 新值
     */
    private Object newValue;
}
