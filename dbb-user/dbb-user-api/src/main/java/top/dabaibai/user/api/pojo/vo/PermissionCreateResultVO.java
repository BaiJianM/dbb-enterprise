package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 角色创建回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 17:28
 */
@Data
@Schema(description = "权限创建回参VO")
public class PermissionCreateResultVO {

    @Schema(description = "权限id")
    private Long permissionId;

}
