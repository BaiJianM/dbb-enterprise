package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 用户信息更新传参
 * @author: 白剑民
 * @dateTime: 2022/10/27 09:35
 */
@Data
@Schema(description = "用户信息更新DTO")
public class UserUpdateDTO {

    @Schema(description = "用户ID")
    @NotNull(message = "用户id，userIdId不能为null")
    @Min(value = 1, message = "用户id，userIdId数值必须大于0")
    private Long userId;

    @Schema(description = "用户编号")
    private String code;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "在职状态（数据字典表枚举）")
    private Integer workingState;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "部门ID")
    private Long departmentId;
}
