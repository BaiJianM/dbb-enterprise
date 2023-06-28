package com.gientech.iot.user.api.pojo.dto;

import com.gientech.iot.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 子系统查询传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "子系统查询传参DTO")
public class SystemSearchDTO extends PageDTO {

    @Schema(description = "子系统名称")
    private String systemName;

    @Schema(description = "子系统服务名（nacos注册名称）")
    private String serviceId;

    @Schema(description = "是否启用")
    private Boolean isEnable;

}
