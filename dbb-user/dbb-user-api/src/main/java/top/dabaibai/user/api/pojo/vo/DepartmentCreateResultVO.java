package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 创建部门回参
 * @author: 白剑民
 * @dateTime: 2022/10/25 09:38
 */
@Data
@Schema(description = "创建部门回参VO")
public class DepartmentCreateResultVO {

    @Schema(description = "部门id")
    private Long departmentId;

}
