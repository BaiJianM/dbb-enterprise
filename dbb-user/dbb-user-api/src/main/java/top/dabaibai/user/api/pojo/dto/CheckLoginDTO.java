package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 登录校验传参
 * @author: 白剑民
 * @dateTime: 2022/10/26 09:45
 */
@Data
@Schema(description = "登录信息校验DTO")
public class CheckLoginDTO {

    @Schema(description = "账号")
    private String username;

    @Schema(description = "手机号")
    private String phone;

}
