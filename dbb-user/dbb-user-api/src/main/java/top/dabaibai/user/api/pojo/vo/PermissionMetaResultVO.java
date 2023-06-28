package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 权限Meta信息出参
 * @author: 白剑民
 * @dateTime: 2023-04-24 17:03:45
 */
@Data
@Schema(description = "权限Meta信息")
public class PermissionMetaResultVO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "图标")
    private String icon;
}
