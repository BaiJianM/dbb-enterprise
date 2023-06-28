package top.dabaibai.user.api.pojo.dto;

import top.dabaibai.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 角色查询授权用户传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色查询授权用户传参DTO")
public class RoleAuthUserSearchDTO extends PageDTO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phone;

}
