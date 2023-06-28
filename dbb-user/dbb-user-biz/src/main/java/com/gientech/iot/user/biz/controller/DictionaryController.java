package com.gientech.iot.user.biz.controller;

import com.gientech.iot.core.utils.BeanConvertUtils;
import com.gientech.iot.user.api.pojo.dto.DictCreateDTO;
import com.gientech.iot.user.api.pojo.dto.DictSearchDTO;
import com.gientech.iot.user.api.pojo.dto.DictUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.DictCreateResultVO;
import com.gientech.iot.user.api.pojo.vo.DictInfoVO;
import com.gientech.iot.user.api.pojo.vo.DictTreeVO;
import com.gientech.iot.user.biz.entity.SysDictionary;
import com.gientech.iot.user.biz.service.DictionaryService;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 数据字典控制层
 * @author: 白剑民
 * @dateTime: 2023/5/2 20:39
 */
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "部门信息相关接口")
@Validated
@Slf4j
public class DictionaryController {

    private final DictionaryService dictionaryService;

    /**
     * @param dto 创建数据字典传参
     * @description: 创建数据字典
     * @author: 白剑民
     * @date: 2023-05-13 13:26:52
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * com.gientech.iot.user.api.pojo.vo.DictCreateResultVO>
     * @version: 1.0
     */
    @Operation(summary = "创建数据字典")
    @PostMapping
    public GientechResponse<DictCreateResultVO> createDict(@Valid @RequestBody DictCreateDTO dto) {
        return GientechResponse.success(dictionaryService.createDict(dto));
    }

    /**
     * @param dictIds 数据字典主键id
     * @description: 根据主键id删除字典信息
     * @author: 白剑民
     * @date: 2023-05-22 09:38:17
     * @return: com.gientech.iot.core.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "根据主键id删除字典信息")
    @Parameter(name = "dictIds", description = "数据字典id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> deleteDictById(
            @NotEmpty(message = "数据字典id列表，dictIds不能为null且数组长度必须大于0")
            @RequestParam List<Long> dictIds) {
        dictionaryService.deleteDictById(dictIds);
        return GientechResponse.success();
    }

    /**
     * @param dto 数据字典更新传参
     * @description: 更新数据字典
     * @author: 白剑民
     * @date: 2023-05-22 13:34:37
     * @return: com.gientech.iot.core.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "更新数据字典")
    @PutMapping
    public GientechResponse<Void> updateDict(@Valid @RequestBody DictUpdateDTO dto) {
        dictionaryService.updateDict(dto);
        return GientechResponse.success();
    }

    /**
     * @param dictId 数据字典主键id
     * @description: 根据主键id获取字典信息
     * @author: 白剑民
     * @date: 2023-05-20 21:22:37
     * @return: com.gientech.iot.core.global.response.GientechResponse<com.gientech.iot.user.api.pojo.vo.DictInfoVO>
     * @version: 1.0
     */
    @Operation(summary = "根据主键id获取字典信息")
    @Parameter(name = "dictId", description = "数据字典id", required = true)
    @GetMapping
    public GientechResponse<DictInfoVO> getDictById(
            @NotNull(message = "数据字典id，dictId不能为null")
            @Min(value = 1, message = "数据字典，dictId数值必须大于0")
            @RequestParam Long dictId) {
        SysDictionary dict = dictionaryService.getDictById(dictId);
        DictInfoVO dictInfoVO =
                BeanConvertUtils.convert(dict, DictInfoVO::new, (s, t) -> {
                            t.setDictId(s.getId());
                            t.setDictParentId(s.getParentId());
                        })
                        .orElse(new DictInfoVO());
        return GientechResponse.success(dictInfoVO);
    }

    /**
     * @param dto 搜索传参
     * @description: 数据字典搜索
     * @author: 白剑民
     * @date: 2023-05-22 14:17:07
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * com.gientech.iot.web.commons.model.PageResultVO < com.gientech.iot.user.api.pojo.vo.DictTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "数据字典搜索")
    @PostMapping("/search")
    public GientechResponse<PageResultVO<DictTreeVO>> search(@Valid @RequestBody DictSearchDTO dto) {
        return GientechResponse.success(dictionaryService.search(dto));
    }

    /**
     * @description: 获取所有数据字典
     * @author: 白剑民
     * @date: 2023-05-24 16:48:38
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * java.util.List < com.gientech.iot.user.api.pojo.vo.DictInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取所有数据字典")
    @GetMapping("/list")
    public GientechResponse<List<DictInfoVO>> listAll() {
        return GientechResponse.success(dictionaryService.listAll());
    }

    /**
     * @description: 获取所有数据字典类型
     * @author: 白剑民
     * @date: 2023-05-25 10:40:03
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * java.util.List < com.gientech.iot.user.api.pojo.vo.DictInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取所有数据字典类型")
    @GetMapping("/listType")
    public GientechResponse<List<DictInfoVO>> listType() {
        return GientechResponse.success(dictionaryService.listType());
    }

    /**
     * @param dictType 数据字典类型
     * @description: 根据数据字典类型获取字典列表
     * @author: 白剑民
     * @date: 2023-05-22 15:10:35
     * @return: com.gientech.iot.core.global.response.GientechResponse<
            * * java.util.List < com.gientech.iot.user.api.pojo.vo.DictTreeVO>>
     * @version: 1.0
     */
    @Operation(summary = "根据数据字典类型获取字典列表")
    @Parameter(name = "dictType", description = "数据字典类型", required = true)
    @GetMapping("/getByType")
    public GientechResponse<List<DictTreeVO>> getDictByType(
            @NotBlank(message = "数据字典类型，dictType不能为null且字符串长度必须大于0")
            @RequestParam String dictType) {
        return GientechResponse.success(dictionaryService.getDictByType(dictType));
    }

    /**
     * @param dictId 数据字典主键id
     * @description: 启用禁用数据字典项
     * @author: 白剑民
     * @date: 2023-05-25 22:55:47
     * @return: com.gientech.iot.core.global.response.GientechResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "启用禁用数据字典项")
    @Parameter(name = "dictId", description = "数据字典id", required = true)
    @PutMapping("/enable")
    public GientechResponse<Void> editStatus(
            @NotNull(message = "数据字典id，dictId不能为null")
            @Min(value = 1, message = "数据字典，dictId数值必须大于0")
            @RequestParam Long dictId) {
        dictionaryService.editStatus(dictId);
        return GientechResponse.success();
    }

    /**
     * @description: 获取当前字典编码的最大编码值
     * @author: 白剑民
     * @date: 2023-05-26 09:42:06
     * @return: com.gientech.iot.core.global.response.GientechResponse<java.lang.Integer>
     * @version: 1.0
     */
    @Operation(summary = "获取当前字典编码的最大编码值")
    @Parameter(name = "dictParentId", description = "父级字典id")
    @GetMapping("/code/max")
    public GientechResponse<Integer> getMaxDictCode(@RequestParam Long dictParentId) {
        return GientechResponse.success(dictionaryService.getMaxDictCode(dictParentId));
    }

}
