package com.gientech.iot.user.biz.controller;

import com.gientech.iot.user.api.pojo.dto.DepartmentCreateDTO;
import com.gientech.iot.user.api.pojo.dto.DepartmentSearchDTO;
import com.gientech.iot.user.api.pojo.dto.DepartmentUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.DepartmentCreateResultVO;
import com.gientech.iot.user.api.pojo.vo.DepartmentDetailResultVO;
import com.gientech.iot.user.api.pojo.vo.DepartmentSearchResultVO;
import com.gientech.iot.user.api.pojo.vo.DepartmentTreeVO;
import com.gientech.iot.user.biz.service.DepartmentService;
import com.gientech.iot.web.commons.http.GientechResponse;
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
     * @return: GientechResponse<String>
     * @version: 1.0
     */
    @Operation(summary = "获取部门编号")
    @GetMapping("/getNo")
    public GientechResponse<String> getNo() {
        return GientechResponse.success(departmentService.getNo());
    }

    /**
     * @param dto 创建部门传参
     * @description: 创建部门
     * @author: 白剑民
     * @date: 2022-10-24 17:39:34
     * @return: com.gientech.iot.global.response.GientechResponse<
            * * com.gientech.iot.user.entity.vo.DepartmentCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "创建部门")
    @PostMapping
    public GientechResponse<DepartmentCreateResultVO> create(@Valid @RequestBody DepartmentCreateDTO dto) {
        return GientechResponse.success(departmentService.create(dto));
    }

    /**
     * @param departmentId 部门id
     * @description: 部门详情
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: GientechResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "部门详情")
    @Parameter(name = "departmentId", description = "部门id", required = true)
    @GetMapping
    public GientechResponse<DepartmentDetailResultVO> detail(
            @NotNull(message = "部门id，departmentId不能为null")
            @Min(value = 1, message = "部门id，departmentId数值必须大于0")
            @RequestParam("departmentId") Long departmentId) {
        return GientechResponse.success(departmentService.detail(departmentId));
    }

    /**
     * @param dto 部门信息更新传参
     * @description: 部门信息更新
     * @author: 白剑民
     * @date: 2022-10-25 09:49:31
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "部门信息更新")
    @PutMapping
    public GientechResponse<Void> update(@Valid @RequestBody DepartmentUpdateDTO dto) {
        departmentService.update(dto);
        return GientechResponse.success();
    }

    /**
     * @param departmentIds 部门id列表
     * @description: 删除部门信息
     * @author: 白剑民
     * @date: 2022-10-26 14:27:00
     * @return: com.gientech.iot.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除部门信息")
    @Parameter(name = "departmentIds", description = "部门id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotEmpty(message = "部门id列表，departmentIds不能为null且数组列表长度必须大于0")
            @RequestParam List<Long> departmentIds) {
        departmentService.delete(departmentIds);
        return GientechResponse.success();
    }

    /**
     * @param dto 查询部门信息传参
     * @description: 查询部门信息列表
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: GientechResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "创建部门")
    @PostMapping("/list")
    public GientechResponse<List<DepartmentSearchResultVO>> list(@Valid @RequestBody DepartmentSearchDTO dto) {
        return GientechResponse.success(departmentService.list(dto));
    }

    /**
     * @param dto 查询部门信息传参
     * @description: 查询部门信息树
     * @author: 白剑民
     * @date: 2023-05-23 14:05:09
     * @return: GientechResponse<PageResultVO < DepartmentSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "查询部门信息树")
    @PostMapping("/tree")
    public GientechResponse<List<DepartmentTreeVO>> tree(@Valid @RequestBody DepartmentSearchDTO dto) {
        return GientechResponse.success(departmentService.tree(dto));
    }

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取部门树
     * @author: 白剑民
     * @date: 2022-10-21 17:06:40
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.DepartmentTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据企业/机构id获取部门树")
    @Parameter(name = "enterpriseId", description = "企业/机构id", required = true)
    @GetMapping("/getTreeByEnterpriseId")
    public GientechResponse<List<DepartmentTreeVO>> getTreeByEnterpriseId(
            @NotNull(message = "企业/机构id，enterpriseId不能为null")
            @Min(value = 1, message = "企业/机构id，enterpriseId数值必须大于0")
            @RequestParam("enterpriseId") Long enterpriseId) {
        return GientechResponse.success(departmentService.getTreeByEnterpriseId(enterpriseId));
    }

    /**
     * @param departmentId 部门id
     * @description: 根据父级部门id获取部门树
     * @author: 白剑民
     * @date: 2022-10-24 16:31:48
     * @return: com.gientech.iot.global.response.GientechResponse<
            * java.util.List < com.gientech.iot.user.entity.vo.DepartmentTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据父级部门id获取部门树")
    @Parameter(name = "departmentId", description = "部门id", required = true)
    @GetMapping("/getTreeByDepartmentId")
    public GientechResponse<List<DepartmentTreeVO>> getTreeByDepartmentId(
            @NotNull(message = "部门id，departmentId不能为null")
            @Min(value = 1, message = "部门id，departmentId数值必须大于0")
            @RequestParam("departmentId") Long departmentId) {
        return GientechResponse.success(departmentService.getTreeByDepartmentId(departmentId));
    }
}
