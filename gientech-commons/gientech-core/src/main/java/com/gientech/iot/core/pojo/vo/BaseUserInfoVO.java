package com.gientech.iot.core.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 基地用户信息出参
 * @author: 王强
 * @dateTime: 2023-05-24 12:15:37
 */
@Data
public class BaseUserInfoVO {

    @Schema(description = "用户id")
    protected Long userId;

    @Schema(description = "用户名")
    protected String username;

    @Schema(description = "姓名")
    protected String realName;

    @Schema(description = "编号")
    protected String code;

    @Schema(description = "手机号")
    protected String phone;

    @Schema(description = "所属部门id")
    protected Long departmentId;

    @Schema(description = "所属部门名称")
    protected String departmentName;

    @Schema(description = "所属部门编号")
    protected String departmentCode;

    @Schema(description = "所属企业/机构id")
    protected Long enterpriseId;

    @Schema(description = "所属企业/机构全称")
    protected String enterpriseFullName;

    @Schema(description = "ip地址")
    protected String ipAddress;

    @Schema(description = "是否是管理员")
    protected Boolean isAdmin;

    @Schema(description = "子系统ID")
    protected Long systemId;
}
