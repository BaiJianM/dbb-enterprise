package com.gientech.iot.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 异步线程执行器自动配置类
 * @author: 白剑民
 * @dateTime: 2022/9/20 21:03
 */
@Slf4j
@Configuration
@EnableAsync
@EnableConfigurationProperties(CustomThreadProperties.class)
public class CustomThreadPoolAutoConfiguration {

    /**
     * @description: 自定义线程池
     * @author: 白剑民
     * @date: 2022-09-20 21:23:33
     * @return: java.util.concurrent.Executor
     * @version: 1.0
     */
    @Bean
    public Executor asyncServiceExecutor(CustomThreadProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(properties.getThreadName());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
