package com.gientech.iot.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 用户注册回参
 * @author: 白剑民
 * @dateTime: 2022/10/28 16:19
 */
@Data
@Schema(description = "用户注册结果VO")
public class UserRegisterResultVO {

    @Schema(description = "用户id")
    private Long userId;
}
