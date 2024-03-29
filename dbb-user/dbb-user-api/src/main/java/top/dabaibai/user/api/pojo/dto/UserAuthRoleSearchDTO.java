package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.web.commons.model.PageDTO;

/**
 * @description: 用户查询授权角色传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户查询授权角色传参DTO")
public class UserAuthRoleSearchDTO extends PageDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色名称")
    private String roleName;

}
