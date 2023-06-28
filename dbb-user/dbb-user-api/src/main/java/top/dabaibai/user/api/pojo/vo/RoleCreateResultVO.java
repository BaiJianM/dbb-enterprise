package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 角色查询回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:23
 */
@Data
@Schema(description = "角色查询回参VO")
public class RoleCreateResultVO {

    @Schema(description = "角色id")
    private Long roleId;

}
