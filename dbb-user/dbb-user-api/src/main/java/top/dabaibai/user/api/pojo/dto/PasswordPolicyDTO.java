package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 密码策略生成DTO
 * @author: 白剑民
 * @dateTime: 2022/10/18 17:48
 */
@Data
@Schema(description = "密码策略DTO")
public class PasswordPolicyDTO {

    @Schema(description = "密码策略id")
    @NotNull(message = "密码策略id，passwordPolicyId不能为null")
    @Min(value = 1, message = "密码策略id，passwordPolicyId数值必须大于0")
    private Long passwordPolicyId;

    @Schema(description = "密码复杂类型（数据字典表枚举，默认：0，简单类型）")
    private Integer complexType;

    @Schema(description = "密码校验表达式（前端使用该表达式校验）")
    private String expression;

    @Schema(description = "密码有效期(单位：天，默认：0，永不过期)")
    private Integer validityPeriod;

    @Schema(description = "过期提醒阈值(单位：天，默认：0，不提醒")
    private Integer remindThreshold;

    @Schema(description = "重试次数(默认3次)")
    private Integer retryNum;

    @Schema(description = "冻结时长(单位: 小时，默认：24小时)")
    private Integer freezeTime;

    @Schema(description = "备注")
    private String remark;
}
