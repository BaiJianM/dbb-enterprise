package com.gientech.iot.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 企业信息统计结果
 * @author: 白剑民
 * @dateTime: 2022/10/26 17:21
 */
@Data
@Schema(description = "企业信息统计VO")
public class EnterpriseCountVO {

    @Schema(description = "部门总数")
    private Integer totalDepartment;

    @Schema(description = "用户总数")
    private Integer totalUser;

}
