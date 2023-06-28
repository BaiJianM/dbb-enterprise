package top.dabaibai.user.biz.entity;

import top.dabaibai.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 部门信息表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:23
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysDepartment extends BaseEntity {
    /**
     * ids
     */
    private String ids;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 企业id
     */
    private Long enterpriseId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 部门编号
     */
    private String departmentCode;
    /**
     * 负责人名称
     */
    private String managerName;
    /**
     * 负责人手机号
     */
    private String managerPhone;
    /**
     * 负责人邮箱
     */
    private String managerEmail;
    /**
     * 是否启用
     */
    private Boolean isEnable;
    /**
     * 部门等级
     */
    private Integer level;
    /**
     * 备注
     */
    private String remark;
}
