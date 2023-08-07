package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.dabaibai.user.biz.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 用户与角色关联Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@Repository
public interface UserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * @param roleIds 角色id列表
     * @description: 获取指定角色列表下的关联用户列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getUserIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * @param userIds 用户id列表
     * @description: 获取指定用户列表下的关联角色列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getRoleIdsByUserIds(@Param("userIds") List<Long> userIds);


    /**
     * @param userIds 用户id列表
     * @description: 删除用户列表中的所有用户关联角色数据
     * @author: 白剑民
     * @date: 2023-08-05 17:34:16
     * @version: 1.0
     */
    void deleteByUserIds(@Param("userIds") List<Long> userIds);
}
