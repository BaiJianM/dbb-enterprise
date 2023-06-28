package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 角色创建回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:23
 */
@Data
@Schema(description = "角色创建回参VO")
public class RoleSearchResultVO {

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编号")
    private String roleCode;

    @Schema(description = "角色等级")
    private Integer level;

    @Schema(description = "是否可用(0: 不可用, 1: 可用)")
    private Boolean isEnable;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "子系统名称")
    private String systemName;

}
