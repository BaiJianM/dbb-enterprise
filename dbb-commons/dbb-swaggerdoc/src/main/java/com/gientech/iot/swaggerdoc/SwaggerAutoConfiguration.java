package com.gientech.iot.swaggerdoc;

import com.gientech.iot.swaggerdoc.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Swagger配置类
 * @author: 白剑民
 * @dateTime: 2022-10-18 13:11:10
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {
}
