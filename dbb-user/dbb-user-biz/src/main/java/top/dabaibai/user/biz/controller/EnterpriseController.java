package top.dabaibai.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dabaibai.user.api.pojo.dto.EnterpriseRegisterDTO;
import top.dabaibai.user.api.pojo.dto.EnterpriseSearchDTO;
import top.dabaibai.user.api.pojo.dto.EnterpriseUpdateDTO;
import top.dabaibai.user.api.pojo.vo.EnterpriseCountVO;
import top.dabaibai.user.api.pojo.vo.EnterpriseRegisterResultVO;
import top.dabaibai.user.api.pojo.vo.EnterpriseSearchResultVO;
import top.dabaibai.user.biz.entity.SysEnterprise;
import top.dabaibai.user.biz.service.EnterpriseService;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.commons.model.PageResultVO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 企业/机构信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "企业/机构信息相关接口")
@Validated
@Slf4j
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    /**
     * @param dto 企业/机构注册传参
     * @description: 企业/机构注册
     * @author: 白剑民
     * @date: 2022-10-20 15:22:51
     * @return: top.dabaibai.global.response.DbbResponse<
            * top.dabaibai.user.entity.vo.EnterpriseRegisterResultVO>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构注册")
    @PostMapping
    public DbbResponse<EnterpriseRegisterResultVO> register(@Valid @RequestBody EnterpriseRegisterDTO dto) {
        return DbbResponse.success(enterpriseService.register(dto));
    }

    /**
     * @param dto 企业/机构信息更新传参
     * @description: 企业/机构信息更新
     * @author: 白剑民
     * @date: 2022-10-24 14:38:46
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息更新")
    @PutMapping
    public DbbResponse<Void> update(@Valid @RequestBody EnterpriseUpdateDTO dto) {
        enterpriseService.update(dto);
        return DbbResponse.success();
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 企业/机构信息删除
     * @author: 白剑民
     * @date: 2022-10-24 15:18:38
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息删除")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        enterpriseService.delete(enterpriseId);
        return DbbResponse.success();
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取企业/机构信息
     * @author: 白剑民
     * @date: 2022-10-21 13:51:05
     * @version: 1.0
     */
    @Operation(summary = "根据企业获取企业/机构信息")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/getById")
    public DbbResponse<SysEnterprise> getEnterpriseInfoById(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return DbbResponse.success(enterpriseService.getEnterpriseInfoById(enterpriseId));
    }

    /**
     * @param uniqueCode 企业/机构社会统一信用代码
     * @description: 根据企业/机构社会统一信用代码获取企业/机构信息
     * @author: 白剑民
     * @date: 2022-10-21 13:51:05
     * @version: 1.0
     */
    @Operation(summary = "根据企业/机构社会统一信用代码获取企业/机构信息")
    @Parameter(name = "uniqueCode", description = "企业/机构社会统一信用代码", required = true)
    @GetMapping("/getByUniqueCode")
    public DbbResponse<SysEnterprise> getEnterpriseInfoByUniqueCode(
            @NotBlank(message = "企业/机构社会统一信用代码，uniqueCode不能为null且字符串长度必须大于0")
            @RequestParam("uniqueCode") String uniqueCode) {
        return DbbResponse.success(enterpriseService.getEnterpriseInfoByUniqueCode(uniqueCode));
    }

    /**
     * @param dto 搜索企业/机构信息列表传参
     * @description: 模糊搜索企业/机构信息列表
     * @author: 白剑民
     * @date: 2022-10-21 14:34:23
     * @return: top.dabaibai.core.global.response.DbbResponse<
            * * top.dabaibai.web.commons.model.PageResultVO <
            * * top.dabaibai.user.api.pojo.vo.EnterpriseSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息列表模糊检索")
    @PostMapping("/search")
    public DbbResponse<PageResultVO<EnterpriseSearchResultVO>> register(@Valid @RequestBody EnterpriseSearchDTO dto) {
        return DbbResponse.success(enterpriseService.search(dto));
    }

    /**
     * @description: 统计所有企业/机构数量
     * @author: 白剑民
     * @date: 2022-10-26 17:33:50
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Integer>
     * @version: 1.0
     */
    @Operation(summary = "统计所有企业/机构数量")
    @GetMapping("/count")
    public DbbResponse<Long> count() {
        return DbbResponse.success(enterpriseService.count());
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 统计企业/机构下的所有部门数和用户数
     * @author: 白剑民
     * @date: 2022-10-26 17:33:53
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.vo.EnterpriseCountVO>
     * @version: 1.0
     */
    @Operation(summary = "统计企业/机构下的所有部门数和用户数")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/countDepartmentAndUser")
    public DbbResponse<EnterpriseCountVO> countDepartmentAndUser(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return DbbResponse.success(enterpriseService.countDepartmentAndUser(enterpriseId));
    }

}
