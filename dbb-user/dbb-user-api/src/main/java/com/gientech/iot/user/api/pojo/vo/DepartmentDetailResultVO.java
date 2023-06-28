package com.gientech.iot.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 部门树VO
 * @author: 白剑民
 * @dateTime: 2022/10/21 15:21
 */
@Data
@Schema(description = "部门查询VO")
public class DepartmentDetailResultVO {

    private static final long serialVersionUID = 1996043039608282286L;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "父级部门id")
    private Long parentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门编码")
    private String departmentCode;

    @Schema(description = "负责人名称")
    private String managerName;

    @Schema(description = "负责人手机号")
    private String managerPhone;

    @Schema(description = "负责人邮箱")
    private String managerEmail;

    @Schema(description = "是否可用")
    private Boolean isEnable;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
