package com.gientech.iot.user.api.pojo.dto;

import com.gientech.iot.user.api.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 创建权限传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 17:30
 */
@Data
@Schema(description = "创建权限传参DTO")
public class PermissionCreateDTO {

    @Schema(description = "应用子系统id")
    @NotNull(message = "应用子系统id，systemId不能为null")
    @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
    private Long systemId;

    @Schema(description = "父级权限id")
    @NotNull(message = "父级权限id，parentId不能为null")
    private Long parentId;

    @Schema(description = "权限类型（数据字典表枚举）")
    @NotNull(message = "权限类型，permissionType不能为null")
    private PermissionTypeEnum permissionType;

    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称，permissionName不能为null且字符串长度必须大于0")
    private String permissionName;

    @Schema(description = "权限编号")
    private String permissionCode;

    @Schema(description = "权限路径")
    private String permissionUrl;

    @Schema(description = "顺序")
    private Integer orderNum;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "meta原数据")
    private PermissionMetaDTO meta;

}
