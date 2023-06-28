package com.gientech.iot.user.biz.enums;


import com.gientech.iot.web.commons.http.ErrorResponse;

/**
 * @description: 用户中心自定义响应异常枚举类
 * @author: 白剑民
 * @date : 2022/7/29 10:27
 */
public enum CustomErrorCodeEnum implements ErrorResponse<Integer> {

    /**
     * 业务异常
     */
    NEED_ENTERPRISE_ID(90001, "密码策略需绑定企业/机构id"),
    ENTERPRISE_UPDATE_ERROR(90002, "企业/机构信息更新出错"),
    ENTERPRISE_HAS_DEPARTMENT(90003, "企业/机构存在绑定部门数据"),
    DEPARTMENT_CREATE_ERROR(90004, "部门创建出错"),
    DEPARTMENT_UPDATE_ERROR(90005, "部门更新出错"),
    DEPARTMENT_HAS_USER(90006, "部门下存在关联用户信息，不允许删除"),
    USER_UPDATE_ERROR(90007, "用户更新出错"),
    ACCOUNT_OR_PASSWORD_ERROR(90008, "用户名或密码错误"),
    DEPARTMENT_HAS_LOCK(90009, "当前企业/机构其他人员正在操作删除部门，请稍后再试"),
    DEPARTMENT_DELETE_ERROR(90010, "部门删除出错"),
    USER_CREATE_ERROR(90011, "用户创建出错"),
    ROLE_UPDATE_ERROR(90012, "角色更新出错"),
    ROLE_NOT_EXIT(90013, "角色信息不存在"),
    ROLE_HAS_USER(90013, "角色存在关联用户，不允许删除"),
    ROLE_HAS_PERMISSION(90014, "角色存在关联权限，不允许删除"),
    ROLE_CREATE_ERROR(90015, "角色创建出错"),
    PERMISSION_CREATE_ERROR(90016, "权限创建出错"),
    PERMISSION_UPDATE_ERROR(90017, "权限更新出错"),
    PERMISSION_HAS_ROLE(90018, "权限存在关联角色，不允许删除"),
    USER_IS_NOT_EXIST(90019, "该用户不存在"),
    LOG_TRANSFER_ERROR(90020, "导出操作日志失败"),
    PARAM_PARENT_NOT_EXIST(90021, "父类字典信息不存在"),
    DICTIONARY_EVENT_NOT_EXIST(90022, "操作事件数据字典不存在"),
    AUTH_CODE_ERROR(90023, "图片验证码生成异常"),
    OLD_PASSWORD_NOT_MATCH_ERROR(90024, "原密码错误"),
    SYSTEM_NOT_EXIST(90025, "应用子系统不存在"),
    CODE_CACHE_ERROR(90026, "编码缓存异常，请稍后再试。"),
    SYSTEM_CREATE_ERROR(90027, "子系统创建出错"),
    PARENT_NOT_EXIT(90028, "父节点不存在"),
    ;


    /**
     * 异常状态码
     */
    private final Integer code;

    /**
     * 异常描述
     */
    private final String describe;

    CustomErrorCodeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDescribe() {
        return this.describe;
    }
}
