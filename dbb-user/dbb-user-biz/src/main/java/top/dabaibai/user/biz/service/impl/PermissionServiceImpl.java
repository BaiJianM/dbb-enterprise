package top.dabaibai.user.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.core.utils.TreeUtils;
import top.dabaibai.user.api.pojo.dto.PermissionCreateDTO;
import top.dabaibai.user.api.pojo.dto.PermissionMetaDTO;
import top.dabaibai.user.api.pojo.dto.PermissionSearchDTO;
import top.dabaibai.user.api.pojo.dto.PermissionUpdateDTO;
import top.dabaibai.user.api.pojo.vo.*;
import top.dabaibai.user.biz.entity.SysPermission;
import top.dabaibai.user.biz.entity.SysRole;
import top.dabaibai.user.biz.entity.SysRolePermission;
import top.dabaibai.user.biz.enums.CustomErrorCodeEnum;
import top.dabaibai.user.biz.mapper.PermissionMapper;
import top.dabaibai.user.biz.service.PermissionService;
import top.dabaibai.user.biz.service.RolePermissionService;
import top.dabaibai.user.biz.service.RoleService;
import top.dabaibai.web.commons.http.DbbException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 权限信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/31 13:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, SysPermission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    private final RolePermissionService rolePermissionService;

    private final RoleService roleService;

    @Override
    public PermissionCreateResultVO create(PermissionCreateDTO dto) {
        SysPermission parent = dto.getParentId() == 0 ? new SysPermission() : permissionMapper.selectById(dto.getParentId());
        if (parent == null) {
            throw new DbbException(CustomErrorCodeEnum.PARENT_NOT_EXIT);
        }
        // 将传参字段转换赋值成权限实体属性
        SysPermission sysPermission = BeanConvertUtils.convert(dto, SysPermission::new, (s, t) -> {
            t.setId(IdWorker.getId());
            t.setMeta(JSON.toJSONString(s.getMeta()));
            t.setParentId(s.getParentId());
            t.setIds(parent.getIds() == null ? String.valueOf(t.getId()) : parent.getIds() + "," + t.getId());
            t.setLevel(t.getIds().split(",").length);
        }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.PERMISSION_CREATE_ERROR));
        permissionMapper.insert(sysPermission);
        // 封装返回内容
        PermissionCreateResultVO resultVO = new PermissionCreateResultVO();
        resultVO.setPermissionId(sysPermission.getId());
        return resultVO;
    }

    @Override
    public PermissionDetailResultVO detail(Long id) {
        SysPermission permission = permissionMapper.selectById(id);
        return BeanConvertUtils.convert(permission, PermissionDetailResultVO::new, (s, t) -> {
            t.setPermissionId(s.getId());
            t.setMeta(JSONObject.parseObject(s.getMeta(), PermissionMetaDTO.class));
        }).orElse(new PermissionDetailResultVO());
    }

    @Override
    public void update(PermissionUpdateDTO dto) {
        SysPermission permission = permissionMapper.selectById(dto.getPermissionId());
        // 判断是否为迁移权限操作（变更父级权限id）
        if (StringUtils.isEmpty(permission.getIds()) || !Objects.equals(permission.getParentId(), dto.getParentId())) {
            SysPermission parent = dto.getParentId() == 0 ? new SysPermission() : permissionMapper.selectById(dto.getParentId());
            if (parent == null) {
                throw new DbbException(CustomErrorCodeEnum.PARENT_NOT_EXIT);
            }
            // 更新权限信息
            SysPermission dept = BeanConvertUtils.convert(dto, SysPermission::new, (s, t) -> {
                t.setId(s.getPermissionId());
                t.setParentId(dto.getParentId());
                t.setIds(parent.getIds() == null ? String.valueOf(t.getId()) : parent.getIds() + "," + t.getId());
                t.setLevel(t.getIds().split(",").length);
                t.setMeta(JSON.toJSONString(s.getMeta()));
            }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.PERMISSION_UPDATE_ERROR));
            // 获取权限下的子权限列表
            List<SysPermission> childPermList =
                    permissionMapper.getPermListByParentIds(Collections.singletonList(dto.getPermissionId()), false);
            // 更新子权限ids
            for (SysPermission child : childPermList) {
                List<String> split = Arrays.asList(child.getIds() == null ? new String[]{""} : child.getIds().split(","));
                List<String> strings = split.subList(split.indexOf(String.valueOf(dept.getId())), split.size());
                strings.set(0, dept.getIds());
                child.setIds(strings.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
            }
            childPermList.add(dept);
            // 批量更新权限信息
            updateBatchById(childPermList);
        } else {
            // 将传参字段转换赋值成权限实体属性
            SysPermission dept = BeanConvertUtils.convert(dto, SysPermission::new, (s, t) -> {
                t.setId(s.getPermissionId());
                t.setMeta(JSON.toJSONString(s.getMeta()));
            }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.PERMISSION_UPDATE_ERROR));
            // 更新单个权限信息
            permissionMapper.updateById(dept);
        }
    }

    @Override
    public void delete(List<Long> permissionIds) {
        List<SysPermission> permList = permissionMapper.getPermListByParentIds(permissionIds, true);
        List<Long> permIds = permList.stream().map(SysPermission::getId).collect(Collectors.toList());
        // 获取指定权限列表下的关联角色列表
        List<Long> permissionList = rolePermissionService.getRoleIdsByPermIds(permIds);
        // 判断权限有无关联角色
        if (permissionList.size() > 0) {
            throw new DbbException(CustomErrorCodeEnum.PERMISSION_HAS_ROLE);
        }
        // 权限逻辑删除
        permissionMapper.deleteBatchIds(permList);
    }

    @Override
    public List<PermissionSearchResultVO> list(PermissionSearchDTO dto) {
        List<SysPermission> list = permissionMapper.list(dto);
        return (List<PermissionSearchResultVO>) BeanConvertUtils.convertCollection(list,
                PermissionSearchResultVO::new, (s, t) -> {
                    t.setPermissionId(s.getId());
                    t.setMeta(JSONObject.parseObject(s.getMeta(), PermissionMetaResultVO.class));
                }).orElse(new ArrayList<>());
    }

    @Override
    public List<PermissionSearchResultVO> getListByRoleIds(List<Long> roleIds) {
        List<SysPermission> permList = permissionMapper.getPermissionListByRoleIds(roleIds);
        return (List<PermissionSearchResultVO>) BeanConvertUtils.convertCollection(permList, PermissionSearchResultVO::new, (s, t) -> {
            t.setPermissionId(s.getId());
            t.setMeta(JSONObject.parseObject(s.getMeta(), PermissionMetaResultVO.class));
        }).orElse(new ArrayList<>());
    }

    @Override
    public List<PermissionTreeVO> treeBySystemId(Long systemId) {
        // 获取所有权限列表
        List<SysPermission> permList = permissionMapper.selectList(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getSystemId, systemId));
        // 返回树状列表
        return TreeUtils.listToTree(permList, PermissionTreeVO::new, (s, t) -> t.setPermissionId(s.getId()));
    }

    @Override
    public TreeSelectVO<PermissionTreeVO> getTreeSelectByRoleId(Long roleId) {
        SysRole role = Optional.ofNullable(roleService.getById(roleId))
                .orElseThrow(() -> new DbbException(CustomErrorCodeEnum.ROLE_NOT_EXIT));
        List<SysPermission> permList = permissionMapper.getPermissionListByRoleIds(Collections.singletonList(roleId));
        List<Long> permIds = permList.stream().map(SysPermission::getId).collect(Collectors.toList());
        return new TreeSelectVO<>(permIds, treeBySystemId(role.getSystemId()));
    }

    @Override
    public void assignPermission(Long roleId, List<Long> permissionIds) {
        // 定义用户与角色关联列表
        List<SysRolePermission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> {
            SysRolePermission rp = new SysRolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            permissions.add(rp);
        });
        // 创建关联信息
        rolePermissionService.saveBatch(permissions);
    }

    @Override
    public void changePermissionStatus(Long permissionId, Boolean isEnable) {
        SysPermission sysPermission = permissionMapper.selectById(permissionId);
        sysPermission.setIsEnable(isEnable);
        permissionMapper.updateById(sysPermission);
    }

    /**
     * @description: 初始化数据
     * @author: 白剑民
     * @date: 2023-05-04 13:00:56
     * @return: void
     * @version: 1.0
     */
    @PostConstruct
    public void initData() {
        log.info("=====================初始化权限数据=====================");
        List<SysPermission> allPermList = permissionMapper.selectList(new LambdaQueryWrapper<SysPermission>()
                .select(SysPermission::getId, SysPermission::getIds, SysPermission::getParentId));
        Map<Long, String> deptIdsMap = new HashMap<>(allPermList.size());
        for (SysPermission perm : allPermList) {
            String parentIds = deptIdsMap.get(perm.getParentId());
            perm.setIds(parentIds == null ? String.valueOf(perm.getId()) : parentIds + "," + perm.getId());
            perm.setLevel(perm.getIds().split(",").length);
            deptIdsMap.put(perm.getId(), perm.getIds());
        }
        updateBatchById(allPermList);
    }
}
