package top.dabaibai.user.api.pojo.vo;

import top.dabaibai.core.pojo.vo.BaseTreeVO;
import top.dabaibai.user.api.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 权限树VO
 * @author: 白剑民
 * @dateTime: 2022/10/31 14:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "权限树VO")
public class PermissionTreeVO extends BaseTreeVO {

    private static final long serialVersionUID = -2094863636304329063L;

    @Schema(description = "权限id")
    private Long permissionId;

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

    @Schema(description = "元数据")
    private String meta;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "是否可用")
    private Boolean isEnable;

    @Schema(description = "应用子系统id")
    private Long systemId;

    @Schema(description = "终端类型")
    private Integer terminalType;

    @Schema(description = "备注")
    private String remark;
}
