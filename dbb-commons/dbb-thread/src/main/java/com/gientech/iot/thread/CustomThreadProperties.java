package com.gientech.iot.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 自定义线程池配置
 * @author: 白剑民
 * @dateTime: 2022/9/20 21:05
 */
@Data
@ConfigurationProperties(prefix = "gientech.thread")
public class CustomThreadProperties {
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数，默认等于机器CPU核心数
     */
    private Integer corePoolSize = CORES;
    /**
     * 最大线程数，默认等于机器CPU核心数 * 2
     */
    private Integer maxPoolSize = CORES * 2;
    /**
     * 队列大小
     */
    private Integer queueCapacity = 100;
    /**
     * 线程名前缀
     */
    private String threadName = "gientech-async-thread-";
}
