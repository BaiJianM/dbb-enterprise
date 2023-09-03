package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dabaibai.user.biz.entity.SysRolePermission;

import java.util.List;

/**
 * @description: 角色与权限关联Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@Repository
public interface RolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * @param roleIds 角色id列表
     * @description: 获取指定角色列表下的关联权限列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getPermIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * @param permIds 权限id列表
     * @description: 获取指定权限列表下的关联角色列表
     * @author: 白剑民
     * @date: 2022-10-31 17:53:34
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<Long> getRoleIdsByPermIds(@Param("permIds") List<Long> permIds);

}
