package com.gientech.iot.log.core;

import com.gientech.iot.log.core.aop.SystemLogAspect;
import com.gientech.iot.log.core.properties.LogProperties;
import com.gientech.iot.log.core.function.CustomFunctionObjectDiff;
import com.gientech.iot.log.core.function.CustomFunctionRegistrar;
import com.gientech.iot.log.core.service.ILogService;
import com.gientech.iot.log.core.service.impl.DefaultLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @description: 日志记录自动配置
 * @author: 王强
 * @dateTime: 2022-09-02 17:23:51
 */
@Slf4j
@Configuration
@Import({SystemLogAspect.class, CustomFunctionObjectDiff.class})
@EnableConfigurationProperties(LogProperties.class)
public class GientechLogAutoConfiguration {

    /**
     * @description: 日志存储实现
     * @author: 白剑民
     * @date: 2023-02-20 17:41:16
     * @return: com.gientech.iot.logRecord.service.ILogRecordService
     * @version: 1.0
     */
    @Bean
    @ConditionalOnMissingBean(ILogService.class)
    public ILogService logService() {
        log.warn("因未提供ILogService接口实现，启用gientech-log模块操作日志默认存储，" +
                "添加了@OperationLog注解生成的日志将存储于本项目根目录下的OperationLog.txt文件中");
        return new DefaultLogServiceImpl();
    }

    /**
     * @description: 注册自定义方法
     * @author: 王强
     * @date: 2022-09-03 22:39:06
     * @return: @return {@code CustomFunctionRegistrar }
     * @version: 1.0
     */
    @Bean
    public CustomFunctionRegistrar registrar() {
        return new CustomFunctionRegistrar();
    }
}
