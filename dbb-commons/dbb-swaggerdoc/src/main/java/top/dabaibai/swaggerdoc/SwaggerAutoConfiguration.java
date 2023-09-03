package top.dabaibai.swaggerdoc;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.dabaibai.swaggerdoc.properties.SwaggerProperties;

/**
 * @description: Swagger配置类
 * @author: 白剑民
 * @dateTime: 2022-10-18 13:11:10
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {
}
