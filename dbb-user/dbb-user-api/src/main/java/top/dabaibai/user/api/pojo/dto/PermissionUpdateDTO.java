package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.dabaibai.user.api.enums.PermissionTypeEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 权限信息更新传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 17:45
 */
@Data
@Schema(description = "权限信息更新DTO")
public class PermissionUpdateDTO {

    @Schema(description = "权限id")
    @NotNull(message = "权限id，permissionId不能为null")
    @Min(value = 1, message = "权限id，permissionId数值必须大于0")
    private Long permissionId;

    @Schema(description = "父级部门id")
    @NotNull(message = "父级部门id，parentId不能为null")
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

    @Schema(description = "权限参数")
    private String permissionParams;

    @Schema(description = "顺序")
    private Integer orderNum;

    @Schema(description = "meta原数据")
    private PermissionMetaDTO meta;

    @Schema(description = "是否可用")
    private Boolean isEnable;

}
