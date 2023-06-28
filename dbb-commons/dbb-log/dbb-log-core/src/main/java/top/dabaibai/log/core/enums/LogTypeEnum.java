package top.dabaibai.log.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 日志类型枚举
 * @author: 白剑民
 * @dateTime: 2022-09-15 09:42:08
 */
@Getter
@AllArgsConstructor
public enum LogTypeEnum {
    /**
     * 登陆日志
     */
    LOGIN_LOG("1", "登陆日志"),
    /**
     * 操作日志
     */
    OPERATE_LOG("2", "操作日志"),
    ;

    /**
     * 代码
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;
}
