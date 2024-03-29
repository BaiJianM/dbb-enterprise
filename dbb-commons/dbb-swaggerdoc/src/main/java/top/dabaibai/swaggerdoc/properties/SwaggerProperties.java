package top.dabaibai.swaggerdoc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 自定义swagger-ui配置
 * @author: 白剑民
 * @dateTime: 2023/5/31 14:51
 */
@Data
@ConfigurationProperties("dbb.swagger")
public class SwaggerProperties {
    /**
     * 标题
     */
    private String title = "大白白后端基础框架API文档";
    /**
     * 描述
     */
    private String description = "提供基础框架服务集成及常用工具封装";
    /**
     * 作者
     */
    private String author = "白剑民";
    /**
     * 联系地址
     */
    private String contactUrl = "https://www.dabaibai.top";
}
