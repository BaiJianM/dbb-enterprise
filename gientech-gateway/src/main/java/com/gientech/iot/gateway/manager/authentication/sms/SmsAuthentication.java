package com.gientech.iot.gateway.manager.authentication.sms;

import com.gientech.iot.gateway.manager.authentication.BaseAuthentication;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 短信登录验证信息
 * @author: 王强
 * @dateTime: 2022-10-27 10:46:18
 */
@Data
public class SmsAuthentication implements BaseAuthentication {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    /**
     * 应用子系统id
     */
    @NotNull(message = "应用子系统id，systemId不能为null")
    @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
    private Long systemId;
}
