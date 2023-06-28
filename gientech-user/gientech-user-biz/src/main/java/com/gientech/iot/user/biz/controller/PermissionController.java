package com.gientech.iot.user.biz.controller;

import com.gientech.iot.user.api.pojo.dto.PermissionCreateDTO;
import com.gientech.iot.user.api.pojo.dto.PermissionSearchDTO;
import com.gientech.iot.user.api.pojo.dto.PermissionUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.*;
import com.gientech.iot.user.biz.service.PermissionService;
import com.gientech.iot.web.commons.http.GientechResponse;
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
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.PermissionCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "创建权限信息")
    @PostMapping
    public GientechResponse<PermissionCreateResultVO> create(@Valid @RequestBody PermissionCreateDTO dto) {
        return GientechResponse.success(permissionService.create(dto));
    }

    /**
     * @param permissionId 权限信息详情传参
     * @description: 权限信息详情
     * @author: 白剑民
     * @date: 2022-10-31 17:44:18
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.PermissionDetailResultVO>
     * @version: 1.0
     */
    @Operation(summary = "权限信息详情")
    @Parameter(name = "permissionId", description = "权限id", required = true)
    @GetMapping
    public GientechResponse<PermissionDetailResultVO> detail(
            @NotNull(message = "权限id，permissionId不能为null")
            @Min(value = 1, message = "权限id，permissionId数值必须大于0")
            @RequestParam("permissionId") Long permissionId) {
        return GientechResponse.success(permissionService.detail(permissionId));
    }

    /**
     * @param dto 权限信息更新传参
     * @description: 权限信息更新
     * @author: 白剑民
     * @date: 2022-10-31 17:50:53
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "权限信息更新")
    @PutMapping
    public GientechResponse<Void> update(@Valid @RequestBody PermissionUpdateDTO dto) {
        permissionService.update(dto);
        return GientechResponse.success();
    }

    /**
     * @param permissionIds 权限id列表
     * @description: 删除权限信息
     * @author: 白剑民
     * @date: 2022-10-31 17:52:28
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除权限信息")
    @Parameter(name = "permissionIds", description = "权限id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotEmpty(message = "权限id列表，permissionIds不能为null且数组列表长度必须大于0")
            @RequestParam("permissionIds") List<Long> permissionIds) {
        permissionService.delete(permissionIds);
        return GientechResponse.success();
    }

    /**
     * @description: 获取权限信息列表
     * @author: 白剑民
     * @date: 2022-10-31 13:42:06
     * @return: com.gientech.iot.global.response.GientechResponse<java.util.List <
            * com.gientech.iot.user.api.pojo.vo.PermissionSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取权限信息列表")
    @PostMapping("/list")
    public GientechResponse<List<PermissionSearchResultVO>> list(@Validated @RequestBody PermissionSearchDTO dto) {
        return GientechResponse.success(permissionService.list(dto));
    }

    /**
     * @param systemId 子系统id
     * @description: 根据角色id获取其下所有权限列表
     * @author: 白剑民
     * @date: 2022-10-31 13:42:06
     * @return: com.gientech.iot.global.response.GientechResponse<java.util.List < com.gientech.iot.user.entity.Permission>>
     * @version: 1.0
     */
    @Operation(summary = "获取子系统下权限树下拉")
    @Parameter(name = "systemId", description = "子系统id", required = true)
    @GetMapping("/treeBySystemId")
    public GientechResponse<List<PermissionTreeVO>> treeBySystemId(
            @NotNull(message = "子系统id，systemId不能为null")
            @Min(value = 1, message = "子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return GientechResponse.success(permissionService.treeBySystemId(systemId));
    }

    /**
     * @param roleId 角色id
     * @description: 根据角色id获取角色其下权限树下拉
     * @author: 白剑民
     * @date: 2023-05-23 10:23:04
     * @return: GientechResponse<TreeSelectVO < PermissionTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取角色其下权限树下拉")
    @Parameter(name = "roleId", description = "角色id", required = true)
    @GetMapping("/treeSelectByRoleId")
    public GientechResponse<TreeSelectVO<PermissionTreeVO>> treeSelectByRoleId(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId) {
        return GientechResponse.success(permissionService.getTreeSelectByRoleId(roleId));
    }

    /**
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     * @description: 分配角色权限
     * @author: 白剑民
     * @date: 2022-10-31 17:01:22
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "分配角色权限")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id", required = true),
            @Parameter(name = "permissionIds", description = "权限id列表", required = true),
    })
    @PutMapping("/assignPermission")
    public GientechResponse<Void> assignRole(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId,
            @NotEmpty(message = "权限id列表，permissionIds不能为null且数组列表长度必须大于0")
            @RequestBody List<Long> permissionIds) {
        permissionService.assignPermission(roleId, permissionIds);
        return GientechResponse.success();
    }

    /**
     * @param permissionId 权限id
     * @param isEnable     启用或禁用
     * @description: 启用或禁用权限
     * @author: 白剑民
     * @date: 2022-10-31 17:09:38
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "启用或禁用权限")
    @Parameters({
            @Parameter(name = "permissionId", description = "权限id", required = true),
            @Parameter(name = "roleId", description = "启用或禁用", required = true),
    })
    @PutMapping("/changeStatus")
    public GientechResponse<Void> changePermissionStatus(
            @NotNull(message = "权限id，permissionId不能为null")
            @Min(value = 1, message = "权限id，permissionId数值必须大于0")
            @RequestParam("permissionId") Long permissionId,
            @NotNull(message = "启用或禁用，isEnable不能为null且为布尔值")
            @RequestParam("isEnable") Boolean isEnable) {
        permissionService.changePermissionStatus(permissionId, isEnable);
        return GientechResponse.success();
    }

}
