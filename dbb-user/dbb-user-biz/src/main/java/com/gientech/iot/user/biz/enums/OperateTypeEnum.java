package com.gientech.iot.user.biz.enums;

/**
 * @description: 操作类型枚举类
 * @author: 白剑民
 * @dateTime: 2023-05-30 12:32:13
 */
public enum OperateTypeEnum {
    /**
     * 用户新增
     */
    USER_INSERT("101001001", "用户新增"),
    /**
     * 用户修改
     */
    USER_UPDATE("101001002", "用户修改"),
    /**
     * 用户删除
     */
    USER_DELETE("101001003", "用户删除"),
    ;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final String code;

    private final String name;

    OperateTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }

}
