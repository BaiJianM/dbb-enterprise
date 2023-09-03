package top.dabaibai.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dabaibai.user.api.pojo.dto.PasswordPolicyDTO;
import top.dabaibai.user.biz.entity.SysPasswordPolicy;
import top.dabaibai.user.biz.service.PasswordPolicyService;
import top.dabaibai.web.commons.http.DbbResponse;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 密码策略控制层
 * @author: 白剑民
 * @dateTime: 2022/10/24 10:20
 */
@RestController
@RequestMapping("/policy")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "密码策略信息相关接口")
@Validated
@Slf4j
public class PasswordPolicyController {

    private final PasswordPolicyService policyService;

    /**
     * @param passwordPolicyId 密码策略id
     * @description: 根据id获取密码策略
     * @author: 白剑民
     * @date: 2022-10-24 10:23:03
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.PasswordPolicy>
     * @version: 1.0
     */
    @Operation(summary = "根据id获取密码策略")
    @Parameter(name = "passwordPolicyId", description = "密码策略id", required = true)
    @GetMapping
    public DbbResponse<SysPasswordPolicy> getByPasswordPolicyId(
            @NotNull(message = "密码策略id，passwordPolicyId不能为null")
            @Min(value = 1, message = "密码策略id，passwordPolicyId数值必须大于0")
            @RequestParam("passwordPolicyId") Long passwordPolicyId) {
        return DbbResponse.success(policyService.getPasswordPolicyById(passwordPolicyId));
    }


    /**
     * @param dto 修改密码策略信息传参
     * @description: 密码策略修改
     * @author: 白剑民
     * @date: 2022-10-24 16:04:56
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "密码策略修改")
    @PutMapping
    public DbbResponse<Void> update(@Valid @RequestBody PasswordPolicyDTO dto) {
        policyService.update(dto);
        return DbbResponse.success();
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取密码策略
     * @author: 白剑民
     * @date: 2022-10-24 10:23:03
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.PasswordPolicy>
     * @version: 1.0
     */
    @Operation(summary = "根据企业/机构id获取密码策略")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/getByEnterpriseId")
    public DbbResponse<SysPasswordPolicy> getPolicyByEnterpriseId(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return DbbResponse.success(policyService.getPolicyByEnterpriseId(enterpriseId));
    }

    /**
     * @description: 获取所有密码策略列表
     * @author: 白剑民
     * @date: 2022-10-24 15:36:11
     * @return: top.dabaibai.global.response.DbbResponse<java.util.List < top.dabaibai.user.entity.PasswordPolicy>>
     * @version: 1.0
     */
    @Operation(summary = "获取所有密码策略列表")
    @GetMapping("/list")
    public DbbResponse<List<SysPasswordPolicy>> getPolicyList() {
        return DbbResponse.success(policyService.getPolicyList());
    }
}
