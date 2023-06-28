package com.gientech.iot.user.api.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gientech.iot.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description: 用户信息检索传参
 * @author: 白剑民
 * @dateTime: 2022/10/26 14:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户信息检索DTO")
public class UserSearchDTO extends PageDTO {

    @Schema(description = "企业/机构名称")
    private String enterpriseName;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门编号")
    private String departmentCode;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "用户编号")
    private String code;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "在职状态（数据字典表枚举）")
    private Integer workingState;

    @Schema(description = "账号是否可用")
    private Boolean isEnable;

    @Schema(description = "是否为管理员")
    private Boolean isAdmin;

    @Schema(description = "创建时间起始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;

    @Schema(description = "创建时间截止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endCreateTime;
}
