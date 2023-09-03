package top.dabaibai.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dabaibai.user.api.pojo.dto.DepartmentCreateDTO;
import top.dabaibai.user.api.pojo.dto.DepartmentSearchDTO;
import top.dabaibai.user.api.pojo.dto.DepartmentUpdateDTO;
import top.dabaibai.user.api.pojo.vo.DepartmentCreateResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentDetailResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentSearchResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentTreeVO;
import top.dabaibai.user.biz.service.DepartmentService;
import top.dabaibai.web.commons.http.DbbResponse;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 部门信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/21 17:03
 */
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "部门信息相关接口")
@Validated
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * @description: 获取部门编号
     * @author: 白剑民
     * @date: 2023-05-23 20:38:09
     * @return: DbbResponse<String>
     * @version: 1.0
     */
    @Operation(summary = "获取部门编号")
    @GetMapping("/getNo")
    public DbbResponse<String> getNo() {
        return DbbResponse.success(departmentService.getNo());
    }

    /**
     * @param dto 创建部门传参
     * @description: 创建部门
     * @author: 白剑民
     * @date: 2022-10-24 17:39:34
     * @return: top.dabaibai.global.response.DbbResponse<
            * * top.dabaibai.user.entity.vo.DepartmentCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "创建部门")
    @PostMapping
    public DbbResponse<DepartmentCreateResultVO> create(@Valid @RequestBody DepartmentCreateDTO dto) {
        return DbbResponse.success(departmentService.create(dto));
    }

    /**
     * @param departmentId 部门id
     * @description: 部门详情
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: DbbResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "部门详情")
    @Parameter(name = "departmentId", description = "部门id", required = true)
    @GetMapping
    public DbbResponse<DepartmentDetailResultVO> detail(
            @NotNull(message = "部门id，departmentId不能为null")
            @Min(value = 1, message = "部门id，departmentId数值必须大于0")
            @RequestParam("departmentId") Long departmentId) {
        return DbbResponse.success(departmentService.detail(departmentId));
    }

    /**
     * @param dto 部门信息更新传参
     * @description: 部门信息更新
     * @author: 白剑民
     * @date: 2022-10-25 09:49:31
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "部门信息更新")
    @PutMapping
    public DbbResponse<Void> update(@Valid @RequestBody DepartmentUpdateDTO dto) {
        departmentService.update(dto);
        return DbbResponse.success();
    }

    /**
     * @param departmentIds 部门id列表
     * @description: 删除部门信息
     * @author: 白剑民
     * @date: 2022-10-26 14:27:00
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除部门信息")
    @Parameter(name = "departmentIds", description = "部门id列表", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotEmpty(message = "部门id列表，departmentIds不能为null且数组列表长度必须大于0")
            @RequestParam List<Long> departmentIds) {
        departmentService.delete(departmentIds);
        return DbbResponse.success();
    }

    /**
     * @param dto 查询部门信息传参
     * @description: 查询部门信息列表
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: DbbResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "创建部门")
    @PostMapping("/list")
    public DbbResponse<List<DepartmentSearchResultVO>> list(@Valid @RequestBody DepartmentSearchDTO dto) {
        return DbbResponse.success(departmentService.list(dto));
    }

    /**
     * @param dto 查询部门信息传参
     * @description: 查询部门信息树
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: DbbResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "查询部门信息树")
    @PostMapping("/tree")
    public DbbResponse<List<DepartmentTreeVO>> tree(@Valid @RequestBody DepartmentSearchDTO dto) {
        return DbbResponse.success(departmentService.tree(dto));
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取部门树
     * @author: 白剑民
     * @date: 2022-10-21 17:06:40
     * @return: top.dabaibai.global.response.DbbResponse<
            * java.util.List < top.dabaibai.user.entity.vo.DepartmentTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据企业/机构id获取部门树")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/getTreeByEnterpriseId")
    public DbbResponse<List<DepartmentTreeVO>> getTreeByEnterpriseId(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return DbbResponse.success(departmentService.getTreeByEnterpriseId(enterpriseId));
    }

    /**
     * @param departmentId 部门id
     * @description: 根据父级部门id获取部门树
     * @author: 白剑民
     * @date: 2022-10-24 16:31:48
     * @return: top.dabaibai.global.response.DbbResponse<
            * java.util.List < top.dabaibai.user.entity.vo.DepartmentTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据父级部门id获取部门树")
    @Parameter(name = "departmentId", description = "部门id", required = true)
    @GetMapping("/getTreeByDepartmentId")
    public DbbResponse<List<DepartmentTreeVO>> getTreeByDepartmentId(
            @NotNull(message = "部门id，departmentId不能为null")
            @Min(value = 1, message = "部门id，departmentId数值必须大于0")
            @RequestParam("departmentId") Long departmentId) {
        return DbbResponse.success(departmentService.getTreeByDepartmentId(departmentId));
    }
}
