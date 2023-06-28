package top.dabaibai.user.biz.entity;

import top.dabaibai.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 企业/机构信息表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:29
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysEnterprise extends BaseEntity {
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 是否是机构
     */
    private Boolean isOrg;
    /**
     * 企业/机构简称
     */
    private String shortName;
    /**
     * 企业/机构全称
     */
    private String fullName;
    /**
     * 社会统一信用代码/组织机构代码
     */
    private String uniqueCode;
    /**
     * 所在省份
     */
    private String province;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 所在区/县
     */
    private String county;
    /**
     * 所在详细地址
     */
    private String address;
    /**
     * 企业/机构邮箱
     */
    private String email;
    /**
     * 联系方式
     */
    private String mobile;
    /**
     * 传真
     */
    private String fax;
    /**
     * 域名
     */
    private String domain;
    /**
     * 法定代表人
     */
    private String legalPerson;
    /**
     * 法定代表人联系方式
     */
    private String legalPersonPhone;
    /**
     * 企业/机构负责人
     */
    private String charger;
    /**
     * 负责人联系方式
     */
    private String chargerPhone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 密码策略id
     */
    private Long passwordPolicyId;
}
