package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 用户信息检索返回结果
 * @author: 白剑民
 * @dateTime: 2022/10/26 15:08
 */
@Data
@Schema(description = "用户信息检索返回结果VO")
public class UserSearchResultVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "用户头像地址")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "在职状态（数据字典表枚举）")
    private String workingState;

    @Schema(description = "是否为管理员")
    private Boolean isAdmin;

    @Schema(description = "账号是否可用")
    private Boolean isEnable;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "起始创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "截止创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
