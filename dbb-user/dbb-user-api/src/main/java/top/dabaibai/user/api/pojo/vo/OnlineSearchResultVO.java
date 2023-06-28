package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 在线用户信息检索返回结果
 * @author: 白剑民
 * @dateTime: 2022/10/26 15:08
 */
@Data
@Schema(description = "在线用户信息检索返回VO")
public class OnlineSearchResultVO {

    @Schema(description = "在线id")
    private Long onlineId;

    @Schema(description = "子系统名称")
    private String systemName;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "登陆时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

}
