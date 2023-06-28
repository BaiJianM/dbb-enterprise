package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 角色信息更新传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 15:13
 */
@Data
@Schema(description = "角色信息更新DTO")
public class RoleUpdateDTO {

    @Schema(description = "角色id")
    @NotNull(message = "角色id，roleId不能为null")
    @Min(value = 1, message = "角色id，roleId数值必须大于0")
    private Long roleId;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称，roleName不能为null且字符串长度必须大于0")
    private String roleName;

    @Schema(description = "角色编号")
    @NotBlank(message = "角色编号，roleCode不能为null且字符串长度必须大于0")
    private String roleCode;

    @Schema(description = "角色等级")
    private Integer level;

    @Schema(description = "是否可用")
    private Boolean isEnable;

    @Schema(description = "菜单id列表")
    private List<Long> menuIds;

    @Schema(description = "备注")
    private String remark;

}
