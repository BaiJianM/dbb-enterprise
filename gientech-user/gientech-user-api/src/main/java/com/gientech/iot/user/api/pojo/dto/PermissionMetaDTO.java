package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 权限Meta信息入参
 * @author: 王强
 * @dateTime: 2023-04-24 17:03:45
 */
@Data
@Schema(description = "权限Meta信息")
public class PermissionMetaDTO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "组建")
    private String component;

    @Schema(description = "图标")
    private String icon;
}
