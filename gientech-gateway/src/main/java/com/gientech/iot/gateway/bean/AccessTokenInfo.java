package com.gientech.iot.gateway.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 访问令牌信息
 * @author: 白剑民
 * @dateTime: 2022-11-04 11:32:06
 */
@Data
@Schema(description = "token实体类")
public class AccessTokenInfo {

    @Schema(description = "前端访问后台时带上,在请求头设置Authorization=Bearer accessToken;注:Bearer与accessToken之间有一个空格")
    private String accessToken;

    @Schema(description = "刷新accessToken时用到,前端暂不关注此字段", hidden = true)
    private String refreshToken;

    @Schema(description = "token类型,前端暂不关注此字段", hidden = true)
    private String tokenType;

    @Schema(description = "token失效时间,单位:秒,前端暂不关注此字段", hidden = true)
    private Long expiresIn;

}
