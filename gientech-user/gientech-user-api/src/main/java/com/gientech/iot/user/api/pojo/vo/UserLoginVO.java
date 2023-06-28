package com.gientech.iot.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description: 用户登录信息
 * @author: 王强
 * @dateTime: 2022-08-16 09:54:28
 */
@Data
@Schema(description = "用户登录信息")
public class UserLoginVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "账号")
    private String realName;

    @Schema(description = "账号是否启用")
    private Boolean isEnable;

    @Schema(description = "重试次数(默认3次)")
    private Integer retryNum;

    @Schema(description = "冻结时长(单位: 小时, 默认24小时)")
    private Integer freezeTime;

    @Schema(description = "角色代码列表")
    private List<String> roleCodeList;

    @Schema(description = "子系统ID")
    private Long systemId;

    @Schema(description = "是否是管理员")
    private Boolean isAdmin;
}
