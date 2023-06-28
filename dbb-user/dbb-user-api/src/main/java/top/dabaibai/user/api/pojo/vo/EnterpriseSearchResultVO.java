package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 搜索企业/机构信息回参
 * @author: 白剑民
 * @dateTime: 2022/10/21 14:30
 */
@Data
@Schema(description = "企业/机构信息搜索结果VO")
public class EnterpriseSearchResultVO {

    @Schema(description = "企业/机构id")
    private Long enterpriseId;

    @Schema(description = "父级企业/机构id")
    private Long parentId;

    @Schema(description = "是否机构")
    private Boolean isOrg;

    @Schema(description = "企业/机构简称")
    private String shortName;

    @Schema(description = "企业/机构全称")
    private String fullName;

    @Schema(description = "社会统一信用代码")
    private String uniqueCode;

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

    @Schema(description = "企业/机构负责人联系方式")
    private String chargerPhone;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人id")
    private String createUserId;

    @Schema(description = "创建人工号")
    private String createUserJobNo;

    @Schema(description = "创建人姓名")
    private String createUserName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Schema(description = "修改人id")
    private String updateUserId;

    @Schema(description = "修改人工号")
    private String updateUserJobNo;

    @Schema(description = "修改人姓名")
    private String updateUserName;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

}
