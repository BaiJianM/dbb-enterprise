package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.dabaibai.user.biz.entity.SysRolePermission;
import top.dabaibai.user.biz.mapper.RolePermissionMapper;
import top.dabaibai.user.biz.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 角色与权限关联实现类
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:56
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, SysRolePermission> implements RolePermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Long> getPermIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            return rolePermissionMapper.getPermIdsByRoleIds(roleIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Long> getRoleIdsByPermIds(List<Long> permIds) {
        if (CollectionUtils.isNotEmpty(permIds)) {
            return rolePermissionMapper.getRoleIdsByPermIds(permIds);
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                    .in(SysRolePermission::getRoleId, roleIds));
        }
    }

    @Override
    public void deleteByPermIds(List<Long> permIds) {
        if (CollectionUtils.isNotEmpty(permIds)) {
            rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                    .in(SysRolePermission::getPermissionId, permIds));
        }
    }
}
