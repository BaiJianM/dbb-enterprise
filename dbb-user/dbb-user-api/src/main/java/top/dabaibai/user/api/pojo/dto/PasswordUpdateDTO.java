package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 用户密码修改传参
 * @author: 白剑民
 * @dateTime: 2022/10/27 10:22
 */
@Data
@Schema(description = "用户密码修改DTO")
public class PasswordUpdateDTO {

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "是否需要校验旧密码")
    @NotNull(message = "是否需要校验旧密码，isNeedCheck不能为null")
    private Boolean isNeedCheck;

    @Schema(description = "新密码")
    @NotBlank(message = "新密码，newPassword不能为null且字符串长度必须大于0")
    private String newPassword;

    @Schema(description = "原密码")
    private String oldPassword;

}
