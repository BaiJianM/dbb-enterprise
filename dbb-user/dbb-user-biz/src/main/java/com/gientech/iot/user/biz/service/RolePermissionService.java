package com.gientech.iot.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gientech.iot.user.biz.entity.SysRolePermission;

import java.util.List;

/**
 * @description: 角色与权限关联接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface RolePermissionService extends IService<SysRolePermission> {

    /**
     * @param roleIds 角色id列表
     * @description: 获取指定角色列表下的关联权限列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getPermIdsByRoleIds(List<Long> roleIds);

    /**
     * @param permIds 权限id列表
     * @description: 获取指定权限列表下的关联角色列表
     * @author: 白剑民
     * @date: 2022-10-31 17:53:34
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getRoleIdsByPermIds(List<Long> permIds);

    /**
     * @param roleIds 角色id列表
     * @description: 删除指定角色列表下的关联权限列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: void
     * @version: 1.0
     */
    void deleteByRoleIds(List<Long> roleIds);

    /**
     * @param permIds 权限id列表
     * @description: 删除指定权限列表下的关联角色列表
     * @author: 白剑民
     * @date: 2022-10-31 17:53:34
     * @return: void
     * @version: 1.0
     */
    void deleteByPermIds(List<Long> permIds);
}
