package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.dabaibai.user.api.pojo.dto.PermissionSearchDTO;
import top.dabaibai.user.api.pojo.vo.PermissionTreeVO;
import top.dabaibai.user.biz.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 权限信息Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/21 15:55
 */
@Repository
public interface PermissionMapper extends BaseMapper<SysPermission> {

    /**
     * @param dto 入参
     * @description: 列表
     * @author: 白剑民
     * @date: 2023-05-29 10:23:54
     * @return: List<SysPermission>
     * @version: 1.0
     */
    List<SysPermission> list(@Param("dto") PermissionSearchDTO dto);

    /**
     * @param parentIds id列表
     * @description: 获取指定根节点下的关联权限列表
     * @author: 白剑民
     * @date: 2022-10-31 15:30:49
     * @return: java.util.List<java.lang.Long>
     * @version: 1.0
     */
    List<SysPermission> getPermListByParentIds(@Param("parentIds") List<Long> parentIds, @Param("showParent") boolean showParent);

    /**
     * @param systemId 应用子系统id
     * @description: 根据应用子系统id获取其下所有权限列表
     * @author: 白剑民
     * @date: 2022-10-31 13:44:50
     * @return: java.util.List<top.dabaibai.user.entity.vo.PermissionTreeVO>
     * @version: 1.0
     */
    List<PermissionTreeVO> getPermissionListBySystemId(@Param("systemId") Long systemId);

    /**
     * @param roleIds 角色id列表
     * @description: 根据角色id列表获取其下所有权限列表
     * @author: 白剑民
     * @date: 2022-10-31 13:44:50
     * @return: java.util.List<top.dabaibai.user.biz.entity.SysPermission>
     * @version: 1.0
     */
    List<SysPermission> getPermissionListByRoleIds(@Param("roleIds") List<Long> roleIds);
}
