package com.gientech.iot.gateway.bean;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 用于生成token的用户信息
 * @author: 王强
 * @dateTime: 2022-10-17 16:02:32
 */
@Data
public class TokenUser {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "账号")
    private String realName;

    @Schema(description = "子系统id")
    private Long systemId;

}
