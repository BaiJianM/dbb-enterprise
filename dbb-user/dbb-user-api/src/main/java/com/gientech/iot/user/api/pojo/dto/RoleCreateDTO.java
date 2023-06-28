package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 创建角色传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@Data
@Schema(description = "创建角色传参DTO")
public class RoleCreateDTO {

    @Schema(description = "应用子系统id")
    @NotNull(message = "应用子系统id，systemId不能为null")
    @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
    private Long systemId;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称，roleName不能为null且字符串长度必须大于0")
    private String roleName;

    @Schema(description = "角色编号")
    @NotBlank(message = "角色编号，roleCode不能为null且字符串长度必须大于0")
    private String roleCode;

    @Schema(description = "角色等级")
    private Integer level;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单id列表")
    private List<Long> menuIds;

    @Schema(description = "是否可用")
    private Boolean isEnable;

}
