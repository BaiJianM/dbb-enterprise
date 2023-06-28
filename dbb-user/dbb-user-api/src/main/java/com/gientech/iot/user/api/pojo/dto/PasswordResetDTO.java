package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 用户密码重置传参
 * @author: 白剑民
 * @dateTime: 2022/10/27 10:22
 */
@Data
@Schema(description = "用户密码重置DTO")
public class PasswordResetDTO {

    @Schema(description = "用户ID")
    @NotNull(message = "用户id，userIdId不能为null")
    @Min(value = 1, message = "用户id，userIdId数值必须大于0")
    private Long userId;

    @Schema(description = "新密码")
    @NotBlank(message = "密码，password不能为null且字符串长度必须大于0")
    private String password;

}
