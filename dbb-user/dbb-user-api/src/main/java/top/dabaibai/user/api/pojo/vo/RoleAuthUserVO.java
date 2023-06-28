package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 角色授权用户回参
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:23
 */
@Data
@Schema(description = "角色授权用户回参VO")
public class RoleAuthUserVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "编号/工号")
    private String code;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否可用(0: 不可用, 1: 可用)")
    private Boolean isEnable;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
