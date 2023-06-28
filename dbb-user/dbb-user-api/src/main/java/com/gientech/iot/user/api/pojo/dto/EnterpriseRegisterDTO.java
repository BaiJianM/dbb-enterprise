package com.gientech.iot.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 企业/机构注册传参
 * @author: 白剑民
 * @dateTime: 2022/10/18 16:38
 */
@Data
@Schema(description = "企业/机构注册DTO")
public class EnterpriseRegisterDTO {

    @Schema(description = "上级企业/机构id")
    private Long parentId;

    @Schema(description = "是否为机构，必填项")
    @NotNull(message = "是否为机构，isOrg不能为null且为布尔值")
    private Boolean isOrg;

    @Schema(description = "企业/机构简称")
    private String shortName;

    @Schema(description = "企业/机构全称")
    @NotBlank(message = "企业/机构全称，fullName不能为null且字符串长度必须大于0")
    private String fullName;

    @Schema(description = "社会统一信用代码/组织机构代码")
    @NotBlank(message = "社会统一信用代码/组织机构代码，uniqueCode不能为null且字符串长度必须大于0")
    private String uniqueCode;

    @Schema(description = "所在地址")
    @NotBlank(message = "所在地址，address不能为null且字符串长度必须大于0")
    private String address;

    @Schema(description = "企业/机构邮箱")
    private String email;

    @Schema(description = "联系方式")
    private String mobile;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "法人联系方式")
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
