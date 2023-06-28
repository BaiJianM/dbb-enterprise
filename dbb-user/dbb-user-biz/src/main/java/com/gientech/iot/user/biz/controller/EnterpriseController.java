package com.gientech.iot.user.biz.controller;

import com.gientech.iot.user.api.pojo.dto.EnterpriseRegisterDTO;
import com.gientech.iot.user.api.pojo.dto.EnterpriseSearchDTO;
import com.gientech.iot.user.api.pojo.dto.EnterpriseUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseCountVO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseRegisterResultVO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO;
import com.gientech.iot.user.biz.entity.SysEnterprise;
import com.gientech.iot.user.biz.service.EnterpriseService;
import com.gientech.iot.web.commons.http.GientechResponse;
import com.gientech.iot.web.commons.model.PageResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * @return: com.gientech.iot.global.response.GientechResponse<
            * com.gientech.iot.user.entity.vo.EnterpriseRegisterResultVO>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构注册")
    @PostMapping
    public GientechResponse<EnterpriseRegisterResultVO> register(@Valid @RequestBody EnterpriseRegisterDTO dto) {
        return GientechResponse.success(enterpriseService.register(dto));
    }

    /**
     * @param dto 企业/机构信息更新传参
     * @description: 企业/机构信息更新
     * @author: 白剑民
     * @date: 2022-10-24 14:38:46
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息更新")
    @PutMapping
    public GientechResponse<Void> update(@Valid @RequestBody EnterpriseUpdateDTO dto) {
        enterpriseService.update(dto);
        return GientechResponse.success();
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 企业/机构信息删除
     * @author: 白剑民
     * @date: 2022-10-24 15:18:38
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息删除")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        enterpriseService.delete(enterpriseId);
        return GientechResponse.success();
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
    public GientechResponse<SysEnterprise> getEnterpriseInfoById(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return GientechResponse.success(enterpriseService.getEnterpriseInfoById(enterpriseId));
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
    public GientechResponse<SysEnterprise> getEnterpriseInfoByUniqueCode(
            @NotBlank(message = "企业/机构社会统一信用代码，uniqueCode不能为null且字符串长度必须大于0")
            @RequestParam("uniqueCode") String uniqueCode) {
        return GientechResponse.success(enterpriseService.getEnterpriseInfoByUniqueCode(uniqueCode));
    }

    /**
     * @param dto 搜索企业/机构信息列表传参
     * @description: 模糊搜索企业/机构信息列表
     * @author: 白剑民
     * @date: 2022-10-21 14:34:23
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * com.gientech.iot.web.commons.model.PageResultVO <
            * * com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "企业/机构信息列表模糊检索")
    @PostMapping("/search")
    public GientechResponse<PageResultVO<EnterpriseSearchResultVO>> register(@Valid @RequestBody EnterpriseSearchDTO dto) {
        return GientechResponse.success(enterpriseService.search(dto));
    }

    /**
     * @description: 统计所有企业/机构数量
     * @author: 白剑民
     * @date: 2022-10-26 17:33:50
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Integer>
     * @version: 1.0
     */
    @Operation(summary = "统计所有企业/机构数量")
    @GetMapping("/count")
    public GientechResponse<Long> count() {
        return GientechResponse.success(enterpriseService.count());
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 统计企业/机构下的所有部门数和用户数
     * @author: 白剑民
     * @date: 2022-10-26 17:33:53
     * @return: com.gientech.iot.global.response.GientechResponse<com.gientech.iot.user.entity.vo.EnterpriseCountVO>
     * @version: 1.0
     */
    @Operation(summary = "统计企业/机构下的所有部门数和用户数")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/countDepartmentAndUser")
    public GientechResponse<EnterpriseCountVO> countDepartmentAndUser(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return GientechResponse.success(enterpriseService.countDepartmentAndUser(enterpriseId));
    }

}
