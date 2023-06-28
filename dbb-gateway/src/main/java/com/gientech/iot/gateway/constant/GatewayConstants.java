package com.gientech.iot.gateway.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 网关常量
 * @author: 白剑民
 * @dateTime: 2023/5/23 16:18
 */
@Slf4j
public class GatewayConstants {

    public interface AuthErrorConstant {

        String UN_LOGIN = "请先登录系统";

        String USERNAME_NOT_FOUND = "用户不存在";

        String TOKEN_EXPIRED = "token已过期，请重新登录";

        String ACCOUNT_DISABLED = "该账户已被禁用，请联系管理员";

        String ACCOUNT_LOCKED = "该账号已被锁定，请联系管理员";

        String ACCOUNT_EXPIRED = "该账号已过期，请联系管理员";

        String LOGIN_EXPIRED = "登录已过期，请重新登录";

        String PASSWORD_ERROR = "用户不存在或密码错误，您还可以再重试%s次";

        String AUTH_CODE_ERROR = "图片验证码错误或已失效，请重新输入";

        String SMS_CODE_ERROR = "短信验证码错误或已失效，请重新获取";

        String FORBIDDEN = "没有访问该资源的权限";

        String ACCOUNT_FREEZE = "因密码输错%s次，您的账号已被冻结，解除冻结的时间为[%s]";

        String SYSTEM_FORBIDDEN = "您暂无访问该系统的权限，请联系管理员";
    }

    public interface AuthConstant {
        /**
         * redis中repeat格式
         */
        String REPEAT_FORMAT = "repeat:%s:%s";

        /**
         * redis中token格式
         */
        String KEY_FORMAT = "token:%s";

        /**
         * redis中手机验证码格式
         */
        String PHONE_FORMAT = "phone:%s";

        /**
         * token类型
         */
        String BEARER = "Bearer ";

        /**
         * redis中token过期时间
         */
        long TOKEN_EXPIRED = 8 * 60 * 60;

        /**
         * 用户账号密码输错后的重试计数缓存，前缀+用户id
         */
        String RETRY_KEY_PREFIX = "login_retry_count:%s";

        /**
         * 被冻结的账号缓存，前缀+用户id
         */
        String ACCOUNT_FREEZE_PREFIX = "account_freeze:%s";
    }

}
