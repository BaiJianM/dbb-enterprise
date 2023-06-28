package top.dabaibai.xxljob;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: xxl-job属性配置类
 * @author: 白剑民
 * @dateTime: 2023/2/20 15:26
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 客户端配置
     */
    private XxlJobAdmin admin;

    /**
     * 执行器配置
     */
    private XxlJobExecutor executor;

    /**
     * 与客户端服务鉴权码一致
     */
    private String accessToken;

}
