package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 企业/机构信息更新传参
 * @author: 白剑民
 * @dateTime: 2022/10/24 10:39
 */
@Data
@Schema(description = "企业/机构信息更新DTO")
public class EnterpriseUpdateDTO {

    @Schema(description = "企业/机构id")
    @NotNull(message = "企业/机构id，enterpriseId不能为null")
    @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
    private Long enterpriseId;

    @Schema(description = "企业/机构简称")
    private String shortName;

    @Schema(description = "所在省份")
    private String province;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "所在区/县")
    private String county;

    @Schema(description = "所在详细地址")
    private String address;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系方式")
    private String mobile;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "法定代表人联系方式")
    private String legalPersonPhone;

    @Schema(description = "企业/机构负责人")
    private String charger;

    @Schema(description = "负责人联系方式")
    private String chargerPhone;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "密码策略id")
    private Long passwordPolicyId;
}
