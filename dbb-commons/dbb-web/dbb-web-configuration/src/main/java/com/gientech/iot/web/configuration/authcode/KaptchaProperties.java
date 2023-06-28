package com.gientech.iot.web.configuration.authcode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 图形验证码配置
 * @author: 白剑民
 * @dateTime: 2023/4/21 14:14
 */
@Data
@ConfigurationProperties(prefix = "gientech.kaptcha")
public class KaptchaProperties {

    /**
     * 是否开启图形验证码验证
     */
    private Boolean isEnable = false;

    /**
     * 图形验证码类型，默认数字类型
     */
    private AuthCodeTypeEnum authCodeType = AuthCodeTypeEnum.MATH;

    /**
     * 图形验证码过期时间，默认五分钟
     */
    public Integer timeout = 5;
}
