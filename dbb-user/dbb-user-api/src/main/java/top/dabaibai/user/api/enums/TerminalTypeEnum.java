package top.dabaibai.user.api.enums;

import lombok.Getter;

/**
 * @description: 权限类型枚举类
 * @author: 白剑民
 * @date : 2023/04/24 13:30
 */
@Getter
public enum TerminalTypeEnum {
    /**
     * PC端
     */
    PC(1),
    /**
     * 微信小程序端
     */
    WECHAT(2),
    /**
     * 手机app IOS端
     */
    APP_IOS(3),
    /**
     * 手机app 安卓端
     */
    APP_ANDROID(4),
    ;

    private final Integer type;

    TerminalTypeEnum(Integer type) {
        this.type = type;
    }
}
