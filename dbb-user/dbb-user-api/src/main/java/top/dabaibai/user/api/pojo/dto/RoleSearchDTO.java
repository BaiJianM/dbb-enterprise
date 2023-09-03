package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.web.commons.model.PageDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 角色查询传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色查询传参DTO")
public class RoleSearchDTO extends PageDTO {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编号")
    private String roleCode;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "子系统ID")
    @NotNull(message = "应用子系统id，systemId不能为null")
    @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
    private Long systemId;

}
