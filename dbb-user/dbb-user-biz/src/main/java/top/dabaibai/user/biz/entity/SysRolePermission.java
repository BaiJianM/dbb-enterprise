package top.dabaibai.user.biz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 角色与权限关联表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 权限id
     */
    private Long permissionId;
}
