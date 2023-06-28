package com.gientech.iot.user.biz.controller;

import com.gientech.iot.user.api.pojo.dto.*;
import com.gientech.iot.user.api.pojo.vo.RoleAuthUserVO;
import com.gientech.iot.user.api.pojo.vo.RoleCreateResultVO;
import com.gientech.iot.user.api.pojo.vo.RoleDetailResultVO;
import com.gientech.iot.user.api.pojo.vo.RoleSearchResultVO;
import com.gientech.iot.user.biz.entity.SysRole;
import com.gientech.iot.user.biz.service.RoleService;
import com.gientech.iot.web.commons.http.GientechResponse;
import com.gientech.iot.web.commons.model.PageResultVO;
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
 * @description: 角色信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/31 11:14
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "角色信息相关接口")
@Validated
@Slf4j
public class RoleController {

    private final RoleService roleService;

    /**
     * @param dto 角色信息创建传参
     * @description: 角色信息创建
     * @author: 白剑民
     * @date: 2022-10-31 16:28:11
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.RoleCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "角色信息创建")
    @PostMapping
    public GientechResponse<RoleCreateResultVO> create(@Valid @RequestBody RoleCreateDTO dto) {
        return GientechResponse.success(roleService.create(dto));
    }

    /**
     * @param roleId 角色id
     * @description: 角色信息详情
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: GientechResponse<RoleDetailResultVO>
     * @version: 1.0
     */
    @Operation(summary = "角色信息详情")
    @Parameter(name = "roleId", description = "角色id", required = true)
    @GetMapping
    public GientechResponse<RoleDetailResultVO> detail(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId) {
        return GientechResponse.success(roleService.detail(roleId));
    }

    /**
     * @param dto 角色信息更新传参
     * @description: 角色信息更新
     * @author: 白剑民
     * @date: 2022-10-31 15:17:42
     * @return: com.gientech.iot.global.response.GientechResponse<java.util.List < com.gientech.iot.user.entity.Role>>
     * @version: 1.0
     */
    @Operation(summary = "角色信息更新")
    @PutMapping
    public GientechResponse<Void> update(@Valid @RequestBody RoleUpdateDTO dto) {
        roleService.update(dto);
        return GientechResponse.success();
    }

    /**
     * @param roleIds 角色id
     * @description: 角色信息删除
     * @author: 白剑民
     * @date: 2022-10-31 16:22:08
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "角色信息删除")
    @Parameter(name = "roleIds", description = "角色id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotEmpty(message = "角色id列表，roleIds不能为null且数组列表长度必须大于0")
            @RequestParam("roleIds") List<Long> roleIds) {
        roleService.delete(roleIds);
        return GientechResponse.success();
    }

    /**
     * @param dto 角色信息列表传参
     * @description: 角色信息列表
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: GientechResponse<List < RoleSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "角色信息列表")
    @PostMapping("/list")
    public GientechResponse<List<RoleSearchResultVO>> list(@Valid @RequestBody RoleSearchDTO dto) {
        return GientechResponse.success(roleService.list(dto));
    }

    /**
     * @param dto 角色信息分页传参
     * @description: 角色信息分页
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: GientechResponse<PageResultVO < RoleSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "角色信息分页")
    @PostMapping("/page")
    public GientechResponse<PageResultVO<RoleSearchResultVO>> page(@Valid @RequestBody RoleSearchDTO dto) {
        return GientechResponse.success(roleService.page(dto));
    }

    /**
     * @param systemId 应用子系统id
     * @description: 根据应用子系统id获取所有角色列表
     * @author: 白剑民
     * @date: 2022-10-31 11:16:57
     * @return: com.gientech.iot.global.response.GientechResponse<java.util.List < com.gientech.iot.user.entity.Role>>
     * @version: 1.0
     */
    @Operation(summary = "获取应用子系统其下所有角色列表")
    @Parameter(name = "systemId", description = "应用子系统id", required = true)
    @GetMapping("/listBySystemId")
    public GientechResponse<List<SysRole>> listBySystemId(
            @NotNull(message = "应用子系统id，systemId不能为null")
            @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return GientechResponse.success(roleService.listBySystemIdAndUserId(systemId, null));
    }

    /**
     * @param userId 用户id
     * @description: 根据用户id获取其下所有角色列表
     * @author: 白剑民
     * @date: 2022-10-31 13:33:33
     * @return: com.gientech.iot.global.response.GientechResponse<java.util.List < com.gientech.iot.user.entity.Role>>
     * @version: 1.0
     */
    @Operation(summary = "获取用户的所有角色列表")
    @Parameter(name = "userId", description = "用户id", required = true)
    @GetMapping("/listByUserId")
    public GientechResponse<List<SysRole>> getRoleListByUserId(
            @NotNull(message = "用户id，userId不能为null")
            @Min(value = 1, message = "用户id，userId数值必须大于0")
            @RequestParam("userId") Long userId) {
        return GientechResponse.success(roleService.listBySystemIdAndUserId(null, userId));
    }

    /**
     * @param dto 查询入参
     * @description: 获取角色授权用户信息分页
     * @author: 白剑民
     * @date: 2023-05-23 12:23:26
     * @return: GientechResponse<PageResultVO < RoleAuthUserVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取角色授权用户分页")
    @PostMapping("/authUser/authPage")
    public GientechResponse<PageResultVO<RoleAuthUserVO>> authUserPage(@Valid @RequestBody RoleAuthUserSearchDTO dto) {
        return GientechResponse.success(roleService.authUserPage(dto));
    }

    /**
     * @param dto 查询入参
     * @description: 获取角色未授权用户信息分页
     * @author: 白剑民
     * @date: 2023-05-23 12:23:26
     * @return: GientechResponse<PageResultVO < RoleAuthUserVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取角色授权用户分页")
    @PostMapping("/authUser/unAuthPage")
    public GientechResponse<PageResultVO<RoleAuthUserVO>> unAuthUserPage(@Valid @RequestBody RoleAuthUserSearchDTO dto) {
        return GientechResponse.success(roleService.unAuthUserPage(dto));
    }

    /**
     * @param dto 取消入参
     * @description: 角色取消授权用户
     * @author: 白剑民
     * @date: 2023-05-23 12:23:26
     * @return: GientechResponse<PageResultVO < RoleAuthUserVO>>
     * @version: 1.0
     */
    @Operation(summary = "角色取消授权用户")
    @PutMapping("/authUser/cancel")
    public GientechResponse<?> authUserCancel(@Valid @RequestBody RoleAuthUserCancelDTO dto) {
        roleService.authUserCancel(dto);
        return GientechResponse.success();
    }

    /**
     * @param dto 入参
     * @description: 分配用户角色
     * @author: 白剑民
     * @date: 2023-05-23 12:47:53
     * @return: GientechResponse<Void>
     * @version: 1.0
     */
    @Operation(summary = "角色授权用户")
    @PutMapping("/authUser/confirm")
    public GientechResponse<Void> authUserConfirm(@Valid @RequestBody RoleAuthUserConfirmDTO dto) {
        roleService.authUserConfirm(dto);
        return GientechResponse.success();
    }

    /**
     * @param roleId   角色id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用角色
     * @author: 白剑民
     * @date: 2022-10-31 17:25:36
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "启用或禁用角色")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id", required = true),
            @Parameter(name = "isEnable", description = "启用或禁用", required = true),
    })
    @PutMapping("/changeStatus")
    public GientechResponse<Void> changeStatus(
            @NotNull(message = "角色id，roleId不能为null")
            @Min(value = 1, message = "角色id，roleId数值必须大于0")
            @RequestParam("roleId") Long roleId,
            @NotNull(message = "启用或禁用，isEnable不能为null且为布尔值")
            @RequestParam("isEnable") Boolean isEnable) {
        roleService.changeStatus(roleId, isEnable);
        return GientechResponse.success();
    }

}
