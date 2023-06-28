package top.dabaibai.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.dabaibai.user.biz.entity.SysUserRole;

import java.util.List;

/**
 * @description: 用户与角色关联接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface UserRoleService extends IService<SysUserRole> {

    /**
     * @param roleIds 角色id列表
     * @description: 获取指定角色列表下的关联用户列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getUserIdsByRoleIds(List<Long> roleIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的关联角色表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getRoleIdsByUserIds(List<Long> userIds);
}
