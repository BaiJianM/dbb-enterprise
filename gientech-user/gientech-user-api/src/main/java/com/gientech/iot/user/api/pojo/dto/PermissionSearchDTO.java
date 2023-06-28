package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 权限信息查询传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 17:45
 */
@Data
@Schema(description = "权限信息查询DTO")
public class PermissionSearchDTO {

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "子系统ID")
    @NotNull(message = "应用子系统id，systemId不能为null")
    @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
    private Long systemId;

    @Schema(description = "排除父节点id")
    private Long excludeParentId;

}
