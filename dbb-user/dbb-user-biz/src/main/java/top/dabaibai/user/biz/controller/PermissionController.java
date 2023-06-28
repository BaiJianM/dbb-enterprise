package top.dabaibai.user.biz.controller;

import top.dabaibai.user.api.pojo.dto.PermissionCreateDTO;
import top.dabaibai.user.api.pojo.dto.PermissionSearchDTO;
import top.dabaibai.user.api.pojo.dto.PermissionUpdateDTO;
import top.dabaibai.user.api.pojo.vo.*;
import top.dabaibai.user.biz.service.PermissionService;
import top.dabaibai.web.commons.http.DbbResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 权限信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/31 13:37
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "权限信息相关接口")
@Validated
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * @param dto 创建权限信息传参
     * @description: 创建权限信息
     * @author: 白剑民
     * @date: 2022-10-31 17:44:18
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.vo.PermissionCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "创建权限信息")
    @PostMapping
    public DbbResponse<PermissionCreateResultVO> create(@Valid @RequestBody PermissionCreateDTO dto) {
        return DbbResponse.success(permissionService.create(dto));
    }

    /**
     * @param permissionId 权限信息详情传参
     * @description: 权限信息详情
     * @author: 白剑民
     * @date: 2022-10-31 17:44:18
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.vo.PermissionDetailResultVO>
     * @version: 1.0
     */
    @Operation(summary = "权限信息详情")
    @Parameter(name = "permissionId", description = "权限id", required = true)
    @GetMapping
    public DbbResponse<PermissionDetailResultVO> detail(
            @NotNull(message = "权限id，permissionId不能为null")
            @Min(value = 1, message = "权限id，permissionId数值必须大于0")
            @RequestParam("permissionId") Long permissionId) {
        return DbbResponse.success(permissionService.detail(permissionId));
    }

    /**
     * @param dto 权限信息更新传参
     * @description: 权限信息更新
     * @author: 白剑民
     * @date: 2022-10-31 17:50:53
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "权限信息更新")
    @PutMapping
    public DbbResponse<Void> update(@Valid @RequestBody PermissionUpdateDTO dto) {
        permissionService.update(dto);
        return DbbResponse.success();
    }

    /**
     * @param permissionIds 权限id列表
     * @description: 删除权限信息
     * @author: 白剑民
     * @date: 2022-10-31 17:52:28
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除权限信息")
    @Parameter(name = "permissionIds", description = "权限id列表", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotEmpty(message = "权限id列表，permissionIds不能为null且数组列表长度必须大于0")
            @RequestParam("permissionIds") List<Long> permissionIds) {
        permissionService.delete(permissionIds);
        return DbbResponse.success();
    }

    /**
     * @description: 获取权限信息列表
     * @author: 白剑民
     * @date: 2022-10-31 13:42:06
     * @return: top.dabaibai.global.response.DbbResponse<java.util.List <
            * top.dabaibai.user.api.pojo.vo.PermissionSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取权限信息列表")
    @PostMapping("/list")
    public DbbResponse<List<PermissionSearchResultVO>> list(@Validated @RequestBody PermissionSearchDTO dto) {
        return DbbResponse.success(permissionService.list(dto));
    }

    /**
     * @param systemId 子系统id
     * @description: 根据角色id获取其下所有权限列表
     * @author: 白剑民
     * @date: 2022-10-31 13:42:06
     * @return: top.dabaibai.global.response.DbbResponse<java.util.List < top.dabaibai.user.entity.Permission>>
     * @version: 1.0
     */
    @Operation(summary = "获取子系统下权限树下拉")
    @Parameter(name = "systemId", description = "子系统id", required = true)
    @GetMapping("/treeBySystemId")
    public DbbResponse<List<PermissionTreeVO>> treeBySystemId(
            @NotNull(message = "子系统id，systemId不能为null")
            @Min(value = 1, message = "子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return DbbResponse.success(permissionService.treeBySystemId(systemId));
    }

    /**
     * @param roleId 角色id
     * @description: 根据角色id获取角色其下权限树下拉
     * @author: 白剑民
     * @date: 2023-05-23 10:23:04
     * @return: DbbResponse<TreeSelectVO < PermissionTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取角色其下权限树下拉")
    @Parameter(name = "roleId", description = "角色id", required = true)
    @GetMapping("/treeSelectByRoleId")
    public DbbResponse<TreeSelectVO<PermissionTreeVO>> treeSelectByRoleId(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId) {
        return DbbResponse.success(permissionService.getTreeSelectByRoleId(roleId));
    }

    /**
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     * @description: 分配角色权限
     * @author: 白剑民
     * @date: 2022-10-31 17:01:22
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "分配角色权限")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id", required = true),
            @Parameter(name = "permissionIds", description = "权限id列表", required = true),
    })
    @PutMapping("/assignPermission")
    public DbbResponse<Void> assignRole(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId,
            @NotEmpty(message = "权限id列表，permissionIds不能为null且数组列表长度必须大于0")
            @RequestBody List<Long> permissionIds) {
        permissionService.assignPermission(roleId, permissionIds);
        return DbbResponse.success();
    }

    /**
     * @param permissionId 权限id
     * @param isEnable     启用或禁用
     * @description: 启用或禁用权限
     * @author: 白剑民
     * @date: 2022-10-31 17:09:38
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "启用或禁用权限")
    @Parameters({
            @Parameter(name = "permissionId", description = "权限id", required = true),
            @Parameter(name = "roleId", description = "启用或禁用", required = true),
    })
    @PutMapping("/changeStatus")
    public DbbResponse<Void> changePermissionStatus(
            @NotNull(message = "权限id，permissionId不能为null")
            @Min(value = 1, message = "权限id，permissionId数值必须大于0")
            @RequestParam("permissionId") Long permissionId,
            @NotNull(message = "启用或禁用，isEnable不能为null且为布尔值")
            @RequestParam("isEnable") Boolean isEnable) {
        permissionService.changePermissionStatus(permissionId, isEnable);
        return DbbResponse.success();
    }

}
