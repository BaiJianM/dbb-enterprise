package com.gientech.iot.user.api.pojo.vo;

import com.gientech.iot.core.pojo.vo.BaseTreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 部门树VO
 * @author: 白剑民
 * @dateTime: 2022/10/21 15:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "部门树VO")
public class DepartmentTreeVO extends BaseTreeVO {

    private static final long serialVersionUID = 1996043039608282286L;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门编码")
    private String departmentCode;

    @Schema(description = "备注")
    private String remark;

}
