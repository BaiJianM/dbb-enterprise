package top.dabaibai.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.dabaibai.user.api.pojo.dto.*;
import top.dabaibai.user.api.pojo.vo.RoleAuthUserVO;
import top.dabaibai.user.api.pojo.vo.RoleCreateResultVO;
import top.dabaibai.user.api.pojo.vo.RoleDetailResultVO;
import top.dabaibai.user.api.pojo.vo.RoleSearchResultVO;
import top.dabaibai.user.biz.entity.SysRole;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 角色信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface RoleService extends IService<SysRole> {

    /**
     * @param dto 创建角色传参
     * @description: 创建角色
     * @author: 白剑民
     * @date: 2022-10-31 16:27:00
     * @return: top.dabaibai.user.entity.vo.RoleCreateResultVO
     * @version: 1.0
     */
    RoleCreateResultVO create(RoleCreateDTO dto);

    /**
     * @param id 角色id
     * @description: 角色详情
     * @author: 白剑民
     * @date: 2022-10-31 15:16:29
     * @version: 1.0
     */
    RoleDetailResultVO detail(Long id);

    /**
     * @param dto 角色信息更新传参
     * @description: 角色信息更新
     * @author: 白剑民
     * @date: 2022-10-31 15:16:29
     * @version: 1.0
     */
    void update(RoleUpdateDTO dto);

    /**
     * @param roleIds 角色id列表
     * @description: 删除角色信息
     * @author: 白剑民
     * @date: 2022-10-31 15:22:14
     * @version: 1.0
     */
    void delete(List<Long> roleIds);

    /**
     * @param dto 入参
     * @description: 角色信息列表
     * @author: 白剑民
     * @date: 2023-05-22 17:32:05
     * @return: List<RoleSearchResultVO>
     * @version: 1.0
     */
    List<RoleSearchResultVO> list(RoleSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 角色信息列表
     * @author: 白剑民
     * @date: 2023-05-22 17:32:05
     * @return: PageResultVO<RoleSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<RoleSearchResultVO> page(RoleSearchDTO dto);

    /**
     * @param systemId 应用子系统id
     * @param userId 用户id
     * @description: 根据应用子系统id用户id获取所有角色列表
     * @author: 白剑民
     * @date: 2022-10-31 11:10:57
     * @return: java.util.List<top.dabaibai.user.entity.Role>
     * @version: 1.0
     */
    List<SysRole> listBySystemIdAndUserId(Long systemId, Long userId);

    /**
     * @param dto 入参
     * @description: 角色授权用户信息列表
     * @author: 白剑民
     * @date: 2023-05-23 12:18:48
     * @return: PageResultVO<RoleAuthUserVO>
     * @version: 1.0
     */
    PageResultVO<RoleAuthUserVO> authUserPage(RoleAuthUserSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户信息列表
     * @author: 白剑民
     * @date: 2023-05-23 12:18:48
     * @return: PageResultVO<RoleAuthUserVO>
     * @version: 1.0
     */
    PageResultVO<RoleAuthUserVO> unAuthUserPage(RoleAuthUserSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户信息列表
     * @author: 白剑民
     * @date: 2023-05-23 12:18:48
     * @return: PageResultVO<RoleAuthUserVO>
     * @version: 1.0
     */
    void authUserCancel(RoleAuthUserCancelDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户
     * @author: 白剑民
     * @date: 2023-05-23 12:43:44
     * @return: void
     * @version: 1.0
     */
    void authUserConfirm(RoleAuthUserConfirmDTO dto);

    /**
     * @param roleId   角色id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用角色
     * @author: 白剑民
     * @date: 2022-10-31 17:24:28
     * @version: 1.0
     */
    void changeStatus(Long roleId, Boolean isEnable);
}
