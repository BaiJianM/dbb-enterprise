package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 查询部门传参
 * @author: 白剑民
 * @dateTime: 2022/10/24 17:27
 */
@Data
@Schema(description = "查询部门传参DTO")
public class DepartmentSearchDTO {

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门状态")
    private Boolean isEnable;

    @Schema(description = "排除父节点id")
    private Long excludeParentId;

}
