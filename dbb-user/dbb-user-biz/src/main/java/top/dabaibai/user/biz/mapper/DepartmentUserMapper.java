package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.dabaibai.user.biz.entity.SysDepartmentUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 部门与用户关联Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/26 14:28
 */
@Repository
public interface DepartmentUserMapper extends BaseMapper<SysDepartmentUser> {

    /**
     * @param deptIds 部门id列表
     * @description: 获取指定部门列表下的关联用户列表
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getUserIdsByDeptIds(@Param("deptIds") List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的关联部门列表
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getDeptIdsByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * @param deptIds 部门id列表
     * @description: 获取指定部门列表下的部门用户列表
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<SysDepartmentUser> getListByDeptIds(@Param("deptIds") List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的用户部门列表
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<SysDepartmentUser> getListByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * @param deptIds 部门id列表
     * @description: 删除指定部门列表下的关联数据
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    void deleteByDeptIds(@Param("deptIds") List<Long> deptIds);

    /**
     * @param userIds 用户id列表
     * @description: 删除指定用户列表下关联部门的数据
     * @author: 白剑民
     * @date: 2022-10-26 14:44:04
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    void deleteByUserIds(@Param("userIds") List<Long> userIds);
}
