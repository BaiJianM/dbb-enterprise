package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 部门信息更新传参
 * @author: 白剑民
 * @dateTime: 2022/10/25 09:42
 */
@Data
@Schema(description = "部门信息更新传参DTO")
public class DepartmentUpdateDTO {

    @Schema(description = "部门id")
    @NotNull(message = "部门id，departmentId不能为null")
    @Min(value = 1, message = "部门id，departmentId数值必须大于0")
    private Long departmentId;

    @Schema(description = "父级部门id")
    @NotNull(message = "父级部门id，parentId不能为null")
    private Long parentId;

    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称，departmentName不能为null且字符串长度必须大于0")
    private String departmentName;

    @Schema(description = "部门编号")
    private String departmentCode;

    @Schema(description = "负责人名称")
    private String managerName;

    @Schema(description = "负责人手机号")
    private String managerPhone;

    @Schema(description = "负责人邮箱")
    private String managerEmail;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否可用")
    private Boolean isEnable;
}
