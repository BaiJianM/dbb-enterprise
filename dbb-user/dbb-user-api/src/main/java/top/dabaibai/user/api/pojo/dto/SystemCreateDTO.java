package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 创建子系统传参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:24
 */
@Data
@Schema(description = "创建子系统传参DTO")
public class SystemCreateDTO {

    @Schema(description = "角色名称")
    private String systemName;

    @Schema(description = " 子系统服务名（nacos注册名称）")
    private String serviceId;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "备注")
    private String remark;

}
