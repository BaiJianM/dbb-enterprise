package com.gientech.iot.user.api.pojo.vo;

import com.gientech.iot.user.api.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 权限查询VO
 * @author: 白剑民
 * @dateTime: 2022/10/31 14:47
 */
@Data
@Schema(description = "权限查询VO")
public class PermissionSearchResultVO {

    private static final long serialVersionUID = -2094863636304329063L;

    @Schema(description = "权限id")
    private Long permissionId;

    @Schema(description = "父级权限id")
    private Long parentId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限类型")
    private PermissionTypeEnum permissionType;

    @Schema(description = "权限编码")
    private String permissionCode;

    @Schema(description = "权限路径")
    private String permissionUrl;

    @Schema(description = "权限参数")
    private String permissionParams;

    @Schema(description = "meta原数据")
    private PermissionMetaResultVO meta;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "是否可用")
    private Boolean isEnable;

    @Schema(description = "备注")
    private String remark;
}
