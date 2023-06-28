package top.dabaibai.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 企业/机构注册回参
 * @author: 白剑民
 * @dateTime: 2022/10/18 16:38
 */
@Data
@Schema(description = "企业/机构注册回参VO")
public class EnterpriseRegisterResultVO {
    @Schema(description = "企业/机构id")
    private Long enterpriseId;
}
