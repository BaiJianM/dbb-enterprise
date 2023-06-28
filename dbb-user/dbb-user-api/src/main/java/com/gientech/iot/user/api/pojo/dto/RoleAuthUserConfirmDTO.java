package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description: 角色授权用户传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@Data
@Schema(description = "角色授权用户传参DTO")
public class RoleAuthUserConfirmDTO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "用户ID列表")
    private List<Long> userIds;

}
