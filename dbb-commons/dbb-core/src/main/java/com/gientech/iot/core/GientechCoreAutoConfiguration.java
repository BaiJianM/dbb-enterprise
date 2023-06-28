package com.gientech.iot.core;

import com.gientech.iot.core.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @description: 核心模块自动配置类
 * @author: 白剑民
 * @dateTime: 2023/6/5 20:12
 */
@Slf4j
@Configuration
@Import(SpringContextUtils.class)
public class GientechCoreAutoConfiguration {
}
