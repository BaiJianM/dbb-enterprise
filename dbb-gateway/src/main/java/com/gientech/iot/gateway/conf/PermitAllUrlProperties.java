package com.gientech.iot.gateway.conf;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 配置指定忽略鉴权接口列表
 * @author: 白剑民
 * @dateTime: 2023/4/4 17:19
 */
@Slf4j
@Data
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.ignore-urls}'.isEmpty()")
@ConfigurationProperties(prefix = "security")
public class PermitAllUrlProperties {
    /**
     * 指定忽略接口url
     */
    private String[] ignoreUrls = {};
}
