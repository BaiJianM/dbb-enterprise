package com.gientech.iot.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: redis自定义配置类
 * @author: 白剑民
 * @dateTime: 2022/8/18 15:44
 */
@Data
@ConfigurationProperties(prefix = "gientech.redis")
public class RedisCustomProperties {

    /**
     * 是否使用Redis作为Spring Cache的缓存管理器实现
     */
    private boolean useSpringCache = false;

    /**
     * Spring Cache自定义配置
     */
    private CustomSpringCacheProperties springCacheProperties = new CustomSpringCacheProperties();
}
