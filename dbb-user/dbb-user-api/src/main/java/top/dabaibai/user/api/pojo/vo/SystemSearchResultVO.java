package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 子系统查询回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:23
 */
@Data
@Schema(description = "子系统查询回参VO")
public class SystemSearchResultVO {

    @Schema(description = "子系统id")
    private Long systemId;

    @Schema(description = "子系统名称")
    private String systemName;

    @Schema(description = " 子系统服务名（nacos注册名称）")
    private String serviceId;

    @Schema(description = "是否可用(0: 不可用, 1: 可用)")
    private Boolean isEnable;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
