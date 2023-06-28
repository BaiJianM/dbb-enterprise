package com.gientech.iot.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gientech.iot.user.biz.entity.SysDepartmentUser;

import java.util.List;

/**
 * @description: 用户与角色关联接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface DepartmentUserService extends IService<SysDepartmentUser> {

    /**
     * @param deptIds 部门id列表
     * @description: 获取指定部门列表下的关联用户列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getUserIdsByDeptIds(List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的关联部门列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getDeptIdsByUserIds(List<Long> userIds);

    /**
     * @param deptIds 部门id列表
     * @description: 获取指定部门列表下的部门用户列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<SysDepartmentUser> getListByDeptIds(List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的用户部门列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<SysDepartmentUser> getListByUserIds(List<Long> userIds);

    /**
     * @param deptIds 部门id列表
     * @description: 删除指定部门列表下的关联数据
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    void deleteByDeptIds(List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 删除指定用户列表下的关联数据
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    void deleteByUserIds(List<Long> userIds);
}
