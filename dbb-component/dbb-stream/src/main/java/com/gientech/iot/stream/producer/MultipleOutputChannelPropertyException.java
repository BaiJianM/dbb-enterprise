package com.gientech.iot.stream.producer;


/**
 * @description: 单个Output通道配置异常
 * @author: 白剑民
 * @dateTime: 2023/3/12 00:38
 */
public class MultipleOutputChannelPropertyException extends RuntimeException {

    private static final long serialVersionUID = -390414489372193094L;

    public MultipleOutputChannelPropertyException(String message) {
        super(message);
    }
}
