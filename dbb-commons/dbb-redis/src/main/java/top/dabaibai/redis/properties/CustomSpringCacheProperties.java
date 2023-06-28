package top.dabaibai.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 自定义Spring Cache配置
 * @author: 白剑民
 * @dateTime: 2023/5/13 14:16
 */
@Data
@ConfigurationProperties(prefix = "dbb.redis.spring-cache-properties")
public class CustomSpringCacheProperties {
    /**
     * 是否使用spring cache配置的（或注解中使用value/cacheNames）作为统一前缀，默认开启，为业务区分
     */
    private boolean useKeyPrefix = true;

    /**
     * key的超时时间（单位：毫秒），默认-1永不过期
     */
    private int keyTtl = -1;

    /**
     * 是否缓存空值，默认设置为false，防止缓存击穿
     */
    private boolean cachingNullValue = false;
}
