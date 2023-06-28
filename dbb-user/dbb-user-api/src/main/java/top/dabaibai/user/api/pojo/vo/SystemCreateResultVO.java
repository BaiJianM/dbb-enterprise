package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 子系统创建回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:23
 */
@Data
@Schema(description = "子系统创建回参VO")
public class SystemCreateResultVO {

    @Schema(description = "子系统id")
    private Long systemId;

}
