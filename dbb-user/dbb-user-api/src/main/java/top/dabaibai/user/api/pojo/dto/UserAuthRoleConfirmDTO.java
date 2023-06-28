package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description: 用户授权角色传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@Data
@Schema(description = "用户授权角色传参DTO")
public class UserAuthRoleConfirmDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

}
