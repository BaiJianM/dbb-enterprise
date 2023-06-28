package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 图形验证码返回值
 * @author: 白剑民
 * @dateTime: 2023/4/21 14:37
 */
@Data
@Schema(description = "图形验证码信息")
public class AuthCodeResultVO {

    @Schema(description = "是否开启图形验证码")
    private Boolean captchaEnabled;

    @Schema(description = "验证码缓存key")
    private String uuid;

    @Schema(description = "验证码图片base64字符串")
    private String img;
}
