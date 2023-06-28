package top.dabaibai.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.dabaibai.user.api.pojo.dto.PermissionCreateDTO;
import top.dabaibai.user.api.pojo.dto.PermissionSearchDTO;
import top.dabaibai.user.api.pojo.dto.PermissionUpdateDTO;
import top.dabaibai.user.api.pojo.vo.*;
import top.dabaibai.user.biz.entity.SysPermission;

import java.util.List;

/**
 * @description: 权限信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface PermissionService extends IService<SysPermission> {

    /**
     * @param dto 创建权限传参
     * @description: 创建权限
     * @author: 白剑民
     * @date: 2022-10-31 17:40:09
     * @return: top.dabaibai.user.entity.vo.PermissionCreateResultVO
     * @version: 1.0
     */
    PermissionCreateResultVO create(PermissionCreateDTO dto);

    /**
     * @param id 权限id
     * @description: 权限详情
     * @author: 白剑民
     * @date: 2022-10-31 17:40:09
     * @return: top.dabaibai.user.entity.vo.PermissionDetailResultVO
     * @version: 1.0
     */
    PermissionDetailResultVO detail(Long id);

    /**
     * @param dto 权限信息更新传参
     * @description: 更新
     * @author: 白剑民
     * @date: 2023-05-22 16:11:17
     * @return: void
     * @version: 1.0
     */
    void update(PermissionUpdateDTO dto);

    /**
     * @param permissionIds 权限id列表
     * @description: 删除权限信息
     * @author: 白剑民
     * @date: 2022-10-31 17:51:25
     * @version: 1.0
     */
    void delete(List<Long> permissionIds);

    /**
     * @description: 权限列表（根据用户权限查询权限列表）
     * @author: 白剑民
     * @date: 2022-10-31 17:51:25
     * @version: 1.0
     */
    List<PermissionSearchResultVO> list(PermissionSearchDTO dto);

    /**
     * @param roleId 角色id
     * @description: 根据角色id获取其下所有权限列表
     * @author: 白剑民
     * @date: 2022-10-31 13:39:20
     * @return: java.util.List<top.dabaibai.user.entity.Permission>
     * @version: 1.0
     */
    List<PermissionSearchResultVO> getListByRoleIds(List<Long> roleIds);

    /**
     * @param systemId 系统标识
     * @description: 根据子系统id获取权限树
     * @author: 白剑民
     * @date: 2023-05-25 11:06:01
     * @return: List<PermissionTreeVO>
     * @version: 1.0
     */
    List<PermissionTreeVO> treeBySystemId(Long systemId);

    /**
     * @param roleId 角色id
     * @description: 通过角色id获取树下拉
     * @author: 白剑民
     * @date: 2023-05-23 09:58:32
     * @return: TreeSelectVO<PermissionTreeVO>
     * @version: 1.0
     */
    TreeSelectVO<PermissionTreeVO> getTreeSelectByRoleId(Long roleId);

    /**
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     * @description: 分配角色权限
     * @author: 白剑民
     * @date: 2022-10-31 16:54:21
     * @version: 1.0
     */
    void assignPermission(Long roleId, List<Long> permissionIds);

    /**
     * @param permissionId 权限id
     * @param isEnable     启用或禁用
     * @description: 启用或禁用权限
     * @author: 白剑民
     * @date: 2022-10-31 17:06:53
     * @version: 1.0
     */
    void changePermissionStatus(Long permissionId, Boolean isEnable);
}
