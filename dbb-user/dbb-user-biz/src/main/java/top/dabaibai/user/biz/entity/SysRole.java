package top.dabaibai.user.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.database.entity.BaseEntity;

/**
 * @description: 角色信息表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:56
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysRole extends BaseEntity {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编号
     */
    private String roleCode;
    /**
     * 角色等级
     */
    private Integer level;
    /**
     * 是否可用(0: 不可用, 1: 可用)
     */
    private Boolean isEnable;
    /**
     * 应用子系统id
     */
    private Long systemId;
    /**
     * 备注
     */
    private String remark;
}
