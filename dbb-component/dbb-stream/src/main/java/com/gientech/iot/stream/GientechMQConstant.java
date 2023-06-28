package com.gientech.iot.stream;

/**
 * @description: 消息常量
 * @author: 白剑民
 * @dateTime: 2023-03-10 16:50:36
 */
public interface GientechMQConstant {
    /**
     * 请求头
     */
    interface Headers {
        /**
         * 标签
         */
        String TAGS = "TAGS";
        /**
         * rocketmq事务消息标头
         */
        String TRANSACTIONAL_ARGS = "TRANSACTIONAL_ARGS";
    }

    /**
     * 配置
     */
    interface Property {
        /**
         * 消息输出通道后缀
         */
        String OUTPUT_CHANNEL_SUFFIX = "-out-0";
    }

    /**
     * stream绑定器类型，当前版本框架仅集成rocketmq、rabbitmq、kafka
     */
    interface BinderType {
        /**
         * rocketmq绑定器
         */
        String ROCKETMQ = "rocketmq";
        /**
         * rabbitmq绑定器
         */
        String RABBIT = "rabbit";
        /**
         * kafka绑定器
         */
        String KAFKA = "kafka";
    }

}
