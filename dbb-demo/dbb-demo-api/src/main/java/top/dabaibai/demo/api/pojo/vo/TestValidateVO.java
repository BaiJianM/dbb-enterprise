package top.dabaibai.demo.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.dabaibai.web.validation.parameter.AuthMobile;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/2/20 15:01
 */
@Slf4j
@Data
@Schema(description = "测试校验注解")
public class TestValidateVO {
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @AuthMobile
    private String mobile;
}
