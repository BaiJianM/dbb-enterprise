package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 修改子系统传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@Data
@Schema(description = "修改子系统传参DTO")
public class SystemUpdateDTO {

    @Schema(description = "子系统ID")
    private Long systemId;

    @Schema(description = "子系统名称")
    private String systemName;

    @Schema(description = " 子系统服务名（nacos注册名称）")
    private String serviceId;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "备注")
    private String remark;

}
