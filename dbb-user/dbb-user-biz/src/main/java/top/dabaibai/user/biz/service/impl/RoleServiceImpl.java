package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.user.api.pojo.dto.*;
import top.dabaibai.user.api.pojo.vo.RoleAuthUserVO;
import top.dabaibai.user.api.pojo.vo.RoleCreateResultVO;
import top.dabaibai.user.api.pojo.vo.RoleDetailResultVO;
import top.dabaibai.user.api.pojo.vo.RoleSearchResultVO;
import top.dabaibai.user.biz.entity.*;
import top.dabaibai.user.biz.enums.CustomErrorCodeEnum;
import top.dabaibai.user.biz.mapper.RoleMapper;
import top.dabaibai.user.biz.service.*;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 角色信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/31 11:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {

    private final RoleMapper roleMapper;

    private final UserRoleService userRoleService;

    private final RolePermissionService rolePermissionService;

    private final UserService userService;

    private final SystemService systemService;

    @Override
    public RoleCreateResultVO create(RoleCreateDTO dto) {
        // 将传参字段转换赋值成角色实体属性
        SysRole sysRole = BeanConvertUtils.convert(dto, SysRole::new)
                .orElseThrow(() -> new DbbException(CustomErrorCodeEnum.ROLE_CREATE_ERROR));
        roleMapper.insert(sysRole);
        // 添加角色关联权限列表
        List<SysRolePermission> rolePermList = dto.getMenuIds().stream()
                .map(p -> new SysRolePermission(sysRole.getId(), p)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(rolePermList)) {
            rolePermissionService.saveBatch(rolePermList);
        }
        // 封装返回内容
        RoleCreateResultVO resultVO = new RoleCreateResultVO();
        resultVO.setRoleId(sysRole.getId());
        return resultVO;
    }

    @Override
    public RoleDetailResultVO detail(Long id) {
        SysRole sysRole = roleMapper.selectById(id);
        return BeanConvertUtils.convert(sysRole, RoleDetailResultVO::new, (s, t) -> {
            t.setRoleId(s.getId());
        }).orElse(new RoleDetailResultVO());
    }

    @Override
    public void update(RoleUpdateDTO dto) {
        // 将传参字段转换赋值成角色实体属性
        SysRole role = BeanConvertUtils.convert(dto, SysRole::new, (s, t) -> t.setId(s.getRoleId())).orElse(new SysRole());
        // 删除角色关联权限列表
        rolePermissionService.deleteByRoleIds(Collections.singletonList(dto.getRoleId()));
        // 添加角色关联权限列表
        List<SysRolePermission> rolePermList = dto.getMenuIds().stream()
                .map(p -> new SysRolePermission(dto.getRoleId(), p)).collect(Collectors.toList());
        rolePermissionService.saveBatch(rolePermList);
        // 更新角色信息
        roleMapper.updateById(role);
    }

    @Override
    public void delete(List<Long> roleIds) {
        // 获取指定角色列表下的关联用户列表
        List<Long> users = userRoleService.getUserIdsByRoleIds(roleIds);
        // 判断角色有无关联用户
        if (users.size() > 0) {
            throw new DbbException(CustomErrorCodeEnum.ROLE_HAS_USER);
        }
        // 删除角色关联权限列表
        rolePermissionService.deleteByRoleIds(roleIds);
        // 角色逻辑删除
        roleMapper.deleteBatchIds(roleIds);
    }

    @Override
    public List<RoleSearchResultVO> list(RoleSearchDTO dto) {
        List<SysSystem> systemList = systemService.list(Wrappers.emptyWrapper());
        Map<Long, String> systemMap = systemList.stream().collect(Collectors.toMap(SysSystem::getId, SysSystem::getSystemName));
        List<SysRole> roleList = roleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .eq(dto.getSystemId() != null, SysRole::getSystemId, dto.getSystemId())
                .like(StringUtils.isNotEmpty(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName())
                .like(StringUtils.isNotEmpty(dto.getRoleCode()), SysRole::getRoleCode, dto.getRoleCode())
                .eq(dto.getIsEnable() != null, SysRole::getIsEnable, dto.getIsEnable()));
        return (List<RoleSearchResultVO>) BeanConvertUtils.convertCollection(roleList, RoleSearchResultVO::new, (s, t) -> {
            t.setRoleId(s.getId());
            t.setSystemName(systemMap.get(s.getSystemId()));
        }).orElse(new ArrayList<>());
    }

    @Override
    public PageResultVO<RoleSearchResultVO> page(RoleSearchDTO dto) {
        List<SysSystem> systemList = systemService.list(Wrappers.emptyWrapper());
        Map<Long, String> systemMap = systemList.stream()
                .collect(Collectors.toMap(SysSystem::getId, SysSystem::getSystemName));
        IPage<SysRole> page = roleMapper.selectPage(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysRole>()
                        .eq(dto.getSystemId() != null, SysRole::getSystemId, dto.getSystemId())
                        .like(StringUtils.isNotEmpty(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName())
                        .like(StringUtils.isNotEmpty(dto.getRoleCode()), SysRole::getRoleCode, dto.getRoleCode())
                        .eq(dto.getIsEnable() != null, SysRole::getIsEnable, dto.getIsEnable())
                        .orderByDesc(SysRole::getCreateTime));
        List<RoleSearchResultVO> list = (List<RoleSearchResultVO>) BeanConvertUtils.convertCollection(page.getRecords(),
                RoleSearchResultVO::new, (s, t) -> {
                    t.setRoleId(s.getId());
                    t.setSystemName(systemMap.get(s.getSystemId()));
                }).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public List<SysRole> listBySystemIdAndUserId(Long systemId, Long userId) {
        return roleMapper.getRoleListBySystemIdAndUserId(systemId, userId);
    }

    @Override
    public PageResultVO<RoleAuthUserVO> authUserPage(RoleAuthUserSearchDTO dto) {
        List<Long> userIds = userRoleService.getUserIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
        Page<SysUser> page = userService.page(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysUser>().eq(CollectionUtils.isEmpty(userIds), SysUser::getId, 0)
                        .in(CollectionUtils.isNotEmpty(userIds), SysUser::getId, userIds)
                        .like(StringUtils.isNotEmpty(dto.getUsername()), SysUser::getUsername, dto.getUsername())
                        .like(StringUtils.isNotEmpty(dto.getPhone()), SysUser::getPhone, dto.getPhone()));
        List<RoleAuthUserVO> list = (List<RoleAuthUserVO>) BeanConvertUtils.convertCollection(page.getRecords(),
                RoleAuthUserVO::new, (s, t) -> t.setUserId(s.getId())).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public PageResultVO<RoleAuthUserVO> unAuthUserPage(RoleAuthUserSearchDTO dto) {
        List<Long> userIds = userRoleService.getUserIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
        Page<SysUser> page = userService.page(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysUser>().notIn(CollectionUtils.isNotEmpty(userIds), SysUser::getId, userIds)
                        .like(StringUtils.isNotEmpty(dto.getUsername()), SysUser::getUsername, dto.getUsername())
                        .like(StringUtils.isNotEmpty(dto.getPhone()), SysUser::getPhone, dto.getPhone()));
        List<RoleAuthUserVO> list = (List<RoleAuthUserVO>) BeanConvertUtils.convertCollection(page.getRecords(),
                RoleAuthUserVO::new, (s, t) -> t.setUserId(s.getId())).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public void authUserCancel(RoleAuthUserCancelDTO dto) {
        userRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, dto.getRoleId())
                .in(SysUserRole::getUserId, dto.getUserIds()));
    }

    @Override
    public void authUserConfirm(RoleAuthUserConfirmDTO dto) {
        List<Long> userIds = userRoleService.getUserIdsByRoleIds(Collections.singletonList(dto.getRoleId()));
        List<SysUserRole> userRoleList = dto.getUserIds().stream()
                .filter(p -> !userIds.contains(p))
                .map(p -> new SysUserRole(p, dto.getRoleId())).collect(Collectors.toList());
        // 创建关联信息
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            userRoleService.saveBatch(userRoleList);
        }
    }

    @Override
    public void changeStatus(Long roleId, Boolean isEnable) {
        SysRole sysRole = roleMapper.selectById(roleId);
        sysRole.setIsEnable(isEnable);
        roleMapper.updateById(sysRole);
    }
}
