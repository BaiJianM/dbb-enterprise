package top.dabaibai.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dabaibai.user.api.pojo.dto.SystemCreateDTO;
import top.dabaibai.user.api.pojo.dto.SystemSearchDTO;
import top.dabaibai.user.api.pojo.dto.SystemUpdateDTO;
import top.dabaibai.user.api.pojo.vo.SystemCreateResultVO;
import top.dabaibai.user.api.pojo.vo.SystemDetailResultVO;
import top.dabaibai.user.api.pojo.vo.SystemSearchResultVO;
import top.dabaibai.user.biz.service.SystemService;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.commons.model.PageResultVO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 子系统信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/31 11:14
 */
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "子系统信息相关接口")
@Validated
@Slf4j
public class SystemController {

    private final SystemService systemService;

    /**
     * @param dto 子系统信息创建传参
     * @description: 子系统信息创建
     * @author: 白剑民
     * @date: 2022-10-31 16:28:11
     * @return: top.dabaibai.global.response.DbbResponse<top.dabaibai.user.entity.vo.SystemCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息创建")
    @PostMapping
    public DbbResponse<SystemCreateResultVO> create(@Valid @RequestBody SystemCreateDTO dto) {
        return DbbResponse.success(systemService.create(dto));
    }

    /**
     * @param systemId 子系统id
     * @description: 子系统信息详情
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: DbbResponse<SystemDetailResultVO>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息详情")
    @Parameter(name = "systemId", description = "子系统id", required = true)
    @GetMapping
    public DbbResponse<SystemDetailResultVO> detail(
            @NotNull(message = "子系统id，systemId不能为null")
            @Min(value = 1, message = "子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId) {
        return DbbResponse.success(systemService.detail(systemId));
    }

    /**
     * @param dto 子系统信息更新传参
     * @description: 子系统信息更新
     * @author: 白剑民
     * @date: 2022-10-31 15:17:42
     * @return: top.dabaibai.global.response.DbbResponse<java.util.List < top.dabaibai.user.entity.System>>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息更新")
    @PutMapping
    public DbbResponse<Void> update(@Valid @RequestBody SystemUpdateDTO dto) {
        systemService.update(dto);
        return DbbResponse.success();
    }

    /**
     * @param systemIds 子系统id
     * @description: 子系统信息删除
     * @author: 白剑民
     * @date: 2022-10-31 16:22:08
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息删除")
    @Parameter(name = "systemIds", description = "子系统id列表", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotEmpty(message = "子系统id列表，systemIds不能为null且数组列表长度必须大于0")
            @RequestParam("systemIds") List<Long> systemIds) {
        systemService.delete(systemIds);
        return DbbResponse.success();
    }

    /**
     * @param dto 子系统信息列表传参
     * @description: 子系统信息列表
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: DbbResponse<List < SystemSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息列表")
    @PostMapping("/list")
    public DbbResponse<List<SystemSearchResultVO>> list(@Valid @RequestBody SystemSearchDTO dto) {
        return DbbResponse.success(systemService.list(dto));
    }

    /**
     * @param dto 子系统信息分页传参
     * @description: 子系统信息分页
     * @author: 白剑民
     * @date: 2023-05-22 17:36:37
     * @return: DbbResponse<PageResultVO < SystemSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "子系统信息分页")
    @PostMapping("/page")
    public DbbResponse<PageResultVO<SystemSearchResultVO>> page(@Valid @RequestBody SystemSearchDTO dto) {
        return DbbResponse.success(systemService.page(dto));
    }

    /**
     * @param systemId 子系统id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用子系统
     * @author: 白剑民
     * @date: 2022-10-31 17:25:36
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "子系统或禁用子系统")
    @Parameters({
            @Parameter(name = "systemId", description = "子系统id", required = true),
            @Parameter(name = "isEnable", description = "启用或禁用", required = true),
    })
    @PutMapping("/changeStatus")
    public DbbResponse<Void> changeStatus(
            @NotNull(message = "子系统id，systemId不能为null")
            @Min(value = 1, message = "子系统id，systemId数值必须大于0")
            @RequestParam("systemId") Long systemId,
            @NotNull(message = "启用或禁用，isEnable不能为null且为布尔值")
            @RequestParam("isEnable") Boolean isEnable) {
        systemService.changeStatus(systemId, isEnable);
        return DbbResponse.success();
    }

}
