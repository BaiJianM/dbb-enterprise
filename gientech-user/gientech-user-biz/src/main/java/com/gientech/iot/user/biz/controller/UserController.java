package com.gientech.iot.user.biz.controller;

import com.gientech.iot.core.pojo.vo.BaseUserInfoVO;
import com.gientech.iot.core.utils.BeanConvertUtils;
import com.gientech.iot.core.utils.DateUtils;
import com.gientech.iot.core.utils.UserInfoUtils;
import com.gientech.iot.log.annotations.OperationLog;
import com.gientech.iot.log.core.context.LogContext;
import com.gientech.iot.log.core.enums.LogTypeEnum;
import com.gientech.iot.user.api.enums.LoginTypeEnum;
import com.gientech.iot.user.api.pojo.dto.*;
import com.gientech.iot.user.api.pojo.vo.*;
import com.gientech.iot.user.biz.entity.SysSystem;
import com.gientech.iot.user.biz.service.SystemService;
import com.gientech.iot.user.biz.service.UserService;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 用户信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "用户信息相关接口")
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    private final SystemService systemService;

    /**
     * @param username 账号
     * @description: 根据账号获取登录信息
     * @author: 白剑民
     * @date: 2023-04-06 14:05:12
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.pojo.vo.UserLoginVO>
     * @version: 1.0
     */
    @Operation(summary = "根据账号获取登录信息")
    @Parameters({
            @Parameter(name = "username", description = "账号", required = true),
            @Parameter(name = "systemId", description = "应用子系统id", required = true),
    })
    @GetMapping("/loginByUsername")
    public UserLoginVO getUserLoginInfoByUsername(
            @NotBlank(message = "账号，username不能为null且字符串长度必须大于0")
            @RequestParam("username") String username,
            @NotNull(message = "应用子系统id，systemId不能为null")
            @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return userService.getUserLoginInfoByUsername(username, systemId);
    }

    /**
     * @param phone 手机号
     * @description: 根据手机号获取登录信息
     * @author: 白剑民
     * @date: 2023-04-06 14:05:45
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.pojo.vo.UserLoginVO>
     * @version: 1.0
     */
    @Operation(summary = "根据手机号获取登录信息")
    @Parameters({
            @Parameter(name = "phone", description = "手机号", required = true),
            @Parameter(name = "systemId", description = "应用子系统id", required = true),
    })
    @GetMapping("/loginByPhone")
    public UserLoginVO getUserLoginInfoByPhone(
            @NotBlank(message = "手机号，phone不能为null且字符串长度必须大于0")
            @RequestParam("phone") String phone,
            @NotNull(message = "应用子系统id，systemId不能为null")
            @Min(value = 1, message = "应用子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return userService.getUserLoginInfoByPhone(phone, systemId);
    }

    /**
     * @description: 根据token获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 16:22:37
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.api.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    @Operation(summary = "根据token获取用户信息，注：此接口应在登录后先行调用")
    @GetMapping("/getByToken")
    public GientechResponse<SystemUserInfoVO> getInfoByToken() {
        return GientechResponse.success(userService.getUserInfoByToken());
    }

    /**
     * @description: 登录成功
     * @author: 白剑民
     * @date: 2023-05-29 12:05:50
     * @return: void
     * @version: 1.0
     */
    @OperationLog(bizEvent = "#event",
            msg = "'【' + #user.realName + '】成功登录【' + #system.systemName + '】系统'",
            operatorId = "#user.userId", operatorCode = "#user.username", operatorName = "#user.realName",
            tag = "#tag")
    @Operation(summary = "登录成功", hidden = true)
    @GetMapping("/loginSuccess")
    public void loginSuccess() {
        BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();
        SysSystem systemInfo = systemService.getById(UserInfoUtils.getSystemId());
        LogContext.putVariables("event", LoginTypeEnum.LOGIN.getCode());
        LogContext.putVariables("tag", LogTypeEnum.LOGIN_LOG.getCode());
        LogContext.putVariables("user", userInfo);
        LogContext.putVariables("system", systemInfo);
    }

    /**
     * @description: 注销成功
     * @author: 白剑民
     * @date: 2023-05-29 12:05:52
     * @return: void
     * @version: 1.0
     */
    @OperationLog(bizEvent = "#event",
            msg = "'【' + #user.realName + '】成功退出【' + #system.systemName + '】系统'",
            operatorId = "#user.userId", operatorCode = "#user.username", operatorName = "#user.realName",
            tag = "#tag")
    @Operation(summary = "注销成功", hidden = true)
    @GetMapping("/logoutSuccess")
    public void logoutSuccess() {
        BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();
        SysSystem systemInfo = systemService.getById(UserInfoUtils.getSystemId());
        LogContext.putVariables("event", LoginTypeEnum.LOGOUT.getCode());
        LogContext.putVariables("tag", LogTypeEnum.LOGIN_LOG.getCode());
        LogContext.putVariables("user", userInfo);
        LogContext.putVariables("system", systemInfo);
    }

    /**
     * @param dto 用户注册传参
     * @description: 用户注册
     * @author: 白剑民
     * @date: 2022-10-28 16:22:37
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.UserRegisterResultVO>
     * @version: 1.0
     */
    @Operation(summary = "用户注册")
    @PostMapping
    public GientechResponse<UserRegisterResultVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        return GientechResponse.success(userService.register(dto));
    }

    /**
     * @param dtoList 批量用户注册传参
     * @description: 批量用户注册
     * @author: 白剑民
     * @date: 2022-10-31 10:55:44
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserRegisterResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "批量用户注册")
    @PostMapping("/batchRegister")
    public GientechResponse<List<UserRegisterResultVO>> batchRegister(@Valid @RequestBody List<UserRegisterDTO> dtoList) {
        return GientechResponse.success(userService.batchRegister(dtoList));
    }

    /**
     * @param userId 用户id
     * @description: 根据id获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:14:21
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    @Operation(summary = "根据id获取用户信息")
    @Parameter(name = "userId", description = "用户id", required = true)
    @GetMapping
    public GientechResponse<UserDetailResultVO> detail(
            @NotNull(message = "用户id，userId不能为null")
            @Min(value = 1, message = "用户id，userId数值必须大于0")
            @RequestParam("userId") Long userId) {
        return GientechResponse.success(userService.getUserInfoById(userId));
    }

    /**
     * @param dto 用户信息更新传参
     * @description: 用户信息更新
     * @author: 白剑民
     * @date: 2022-10-27 09:41:48
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "用户信息更新")
    @PutMapping
    public GientechResponse<Void> update(@Valid @RequestBody UserUpdateDTO dto) {
        userService.update(dto);
        return GientechResponse.success();
    }

    /**
     * @param userIds 用户id列表
     * @description: 删除用户信息
     * @author: 白剑民
     * @date: 2022-10-28 15:21:33
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除用户信息")
    @Parameter(name = "userIds", description = "用户id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotEmpty(message = "用户id列表，userIds不能为null且数组列表长度必须大于0")
            @RequestParam List<Long> userIds) {
        userService.delete(userIds);
        return GientechResponse.success();
    }

    /**
     * @param dto 获取用户分页入参
     * @description: 获取用户分页
     * @author: 白剑民
     * @date: 2023-05-24 10:01:22
     * @return: GientechResponse<PageResultVO < UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取用户分页")
    @PostMapping("/page")
    public GientechResponse<PageResultVO<UserSearchResultVO>> page(@Validated @RequestBody UserSearchDTO dto) {
        return GientechResponse.success(userService.page(dto));
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 获取企业/机构其下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-24 17:01:50
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取企业/机构其下所有用户列表")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/listByEnterpriseId")
    public GientechResponse<List<UserDetailResultVO>> getUserListByEnterpriseId(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return GientechResponse.success(userService.getUserListByEnterpriseId(enterpriseId));
    }

    /**
     * @param departmentId 部门id
     * @description: 获取指定部门下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-24 17:03:22
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取指定部门下所有用户列表")
    @Parameter(name = "departmentId", description = "部门id", required = true)
    @GetMapping("/listByDepartmentId")
    public GientechResponse<List<UserDetailResultVO>> listByDepartmentId(
            @NotNull(message = "部门id，departmentId不能为null")
            @Min(value = 1, message = "部门id，departmentId数值必须大于0")
            @RequestParam("departmentId") Long departmentId) {
        return GientechResponse.success(userService.getUserListByDepartmentId(departmentId));
    }

    /**
     * @param userIds 用户id列表
     * @description:
     * @author: 白剑民
     * @date: 2022-10-28 15:59:25
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据id列表获取用户信息")
    @Parameter(name = "userIds", description = "用户id列表", required = true)
    @PostMapping("/getByIds")
    public GientechResponse<List<UserDetailResultVO>> getUserInfoByIds(
            @NotEmpty(message = "用户id列表，userIds不能为null且数组列表长度必须大于0")
            @RequestBody List<Long> userIds) {
        return GientechResponse.success(userService.getUserInfoByIds(userIds));
    }

    /**
     * @param idCardNo 身份证号
     * @description: 根据身份证号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:14:08
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    @Operation(summary = "根据身份证号获取用户信息")
    @Parameter(name = "idCardNo", description = "用户身份证号", required = true)
    @GetMapping("/getByIdCardNo")
    public GientechResponse<UserDetailResultVO> getUserInfoByIdCardNo(
            @NotBlank(message = "用户身份证号，idCardNo不能为null且字符串长度必须大于0")
            @RequestParam("idCardNo") String idCardNo) {
        return GientechResponse.success(userService.getUserInfoByIdCardNo(idCardNo));
    }

    /**
     * @param idCardNos 身份证证号列表
     * @description: 根据身份证号列表获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 16:15:51
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据身份证号列表获取用户信息")
    @Parameter(name = "idCardNos", description = "用户身份证号列表", required = true)
    @PostMapping("/getByIdCardNos")
    public GientechResponse<List<UserDetailResultVO>> getUserInfoByIdCardNos(
            @NotEmpty(message = "用户身份证号列表，idCardNos不能为null且数组列表长度必须大于0")
            @RequestBody List<String> idCardNos) {
        return GientechResponse.success(userService.getUserInfoByIdCardNos(idCardNos));
    }

    /**
     * @param code 用户编号
     * @description: 根据用户编号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 15:36:00
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    @Operation(summary = "根据用户编号获取用户信息")
    @Parameter(name = "code", description = "用户编号", required = true)
    @GetMapping("/getByCode")
    public GientechResponse<UserDetailResultVO> getUserInfoByCode(
            @NotBlank(message = "用户编号，code不能为null且字符串长度必须大于0")
            @RequestParam("code") String code) {
        return GientechResponse.success(userService.getUserInfoByCode(code));
    }

    /**
     * @param codes 用户编号列表
     * @description: 根据用户编号列表获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 16:17:19
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据用户编号列表获取用户信息")
    @Parameter(name = "codes", description = "用户编号列表", required = true)
    @PostMapping("/getByCodes")
    public GientechResponse<List<UserDetailResultVO>> getUserInfoByCodes(
            @NotEmpty(message = "用户编号列表，codes不能为null且数组列表长度必须大于0")
            @RequestBody List<String> codes) {
        return GientechResponse.success(userService.getUserInfoByCodes(codes));
    }

    /**
     * @param userId 用户id
     * @description: 判断用户是否为管理员
     * @author: 白剑民
     * @date: 2022-10-27 10:00:41
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Boolean>
     * @version: 1.0
     */
    @Operation(summary = "判断用户是否为管理员")
    @Parameter(name = "userId", description = "用户id", required = true)
    @GetMapping("/checkIsAdmin")
    public GientechResponse<Boolean> checkIsAdmin(
            @NotNull(message = "用户id，userId不能为null")
            @Min(value = 1, message = "用户id，userId数值必须大于0")
            @RequestParam("userId") Long userId) {
        return GientechResponse.success(userService.checkIsAdmin(userId));
    }

    /**
     * @param userId   用户id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用用户账号
     * @author: 白剑民
     * @date: 2022-10-27 10:12:28
     * @version: 1.0
     */
    @Operation(summary = "启用或禁用用户账号")
    @Parameters({
            @Parameter(name = "userId", description = "用户id", required = true),
            @Parameter(name = "isEnable", description = "启用或禁用", required = true),
    })
    @PutMapping("/changeStatus")
    public GientechResponse<Void> changeStatus(
            @NotNull(message = "用户id，userId不能为null")
            @Min(value = 1, message = "用户id，userId数值必须大于0")
            @RequestParam("userId") Long userId,
            @NotNull(message = "启用或禁用，isEnable不能为null且为布尔值")
            @RequestParam("isEnable") Boolean isEnable) {
        userService.changeStatus(userId, isEnable);
        return GientechResponse.success();
    }

    /**
     * @param dto 用户密码重置传参
     * @description: 用户密码重置
     * @author: 白剑民
     * @date: 2022-10-27 10:34:36
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "用户密码重置")
    @PutMapping("/resetPassword")
    public GientechResponse<Void> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        PasswordUpdateDTO passwordUpdateDTO = BeanConvertUtils.convert(dto, PasswordUpdateDTO::new, (s, t) -> {
            t.setNewPassword(dto.getPassword());
            t.setIsNeedCheck(Boolean.FALSE);
        }).orElse(new PasswordUpdateDTO());
        userService.updatePassword(passwordUpdateDTO);
        return GientechResponse.success();
    }

    /**
     * @param dto 用户密码修改传参
     * @description: 用户密码修改
     * @author: 白剑民
     * @date: 2022-10-27 10:34:36
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "用户密码修改")
    @PutMapping("/updatePassword")
    public GientechResponse<Void> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        dto.setUserId(UserInfoUtils.getUserId());
        userService.updatePassword(dto);
        return GientechResponse.success();
    }

    /**
     * @param dto 查询入参
     * @description: 获取用户授权角色信息分页
     * @author: 白剑民
     * @date: 2023-05-23 12:23:26
     * @return: GientechResponse<PageResultVO < RoleAuthUserVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取用户授权角色分页")
    @PostMapping("/authRole/authPage")
    public GientechResponse<PageResultVO<UserAuthRoleVO>> authUserPage(@Valid @RequestBody UserAuthRoleSearchDTO dto) {
        return GientechResponse.success(userService.authRolePage(dto));
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
    @PostMapping("/authRole/unAuthPage")
    public GientechResponse<PageResultVO<UserAuthRoleVO>> unAuthUserPage(@Valid @RequestBody UserAuthRoleSearchDTO dto) {
        return GientechResponse.success(userService.unAuthRolePage(dto));
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
    @PutMapping("/authRole/cancel")
    public GientechResponse<?> authUserCancel(@Valid @RequestBody UserAuthRoleCancelDTO dto) {
        userService.authRoleCancel(dto);
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
    @PutMapping("/authRole/confirm")
    public GientechResponse<Void> authUserConfirm(@Valid @RequestBody UserAuthRoleConfirmDTO dto) {
        userService.authRoleConfirm(dto);
        return GientechResponse.success();
    }
}
