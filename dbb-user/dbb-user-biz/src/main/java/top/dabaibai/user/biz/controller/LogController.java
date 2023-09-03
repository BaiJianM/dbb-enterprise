package top.dabaibai.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dabaibai.log.core.enums.LogTypeEnum;
import top.dabaibai.user.api.pojo.dto.LogSearchDTO;
import top.dabaibai.user.biz.service.LogService;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.commons.model.PageResultVO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @description: 日志信息控制层
 * @author: 白剑民
 * @dateTime: 2022/11/18 15:06
 */
@RestController
@RequestMapping("log/{logType}")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@Slf4j
@Tag(name = "系统登陆志相关接口")
public class LogController {

    private final LogService logService;

    /**
     * @param logType 日志类型
     * @param dto     日志搜索传参
     * @description: 日志搜索
     * @author: 白剑民
     * @date: 2023-05-29 17:11:23
     * @return: DbbResponse<PageResultVO < LogSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "日志搜索")
    @PostMapping("/search")
    public DbbResponse<PageResultVO<?>> search(@PathVariable("logType") LogTypeEnum logType,
                                               @Valid @RequestBody LogSearchDTO dto) {
        dto.setLogType(logType);
        return DbbResponse.success(logService.search(dto));
    }

    /**
     * @param logIds 日志id
     * @description: 日志删除
     * @author: 白剑民
     * @date: 2023-05-29 17:12:24
     * @return: DbbResponse<Void>
     * @version: 1.0
     */
    @Operation(summary = "日志删除")
    @Parameter(name = "logIds", description = "日志id列表", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotEmpty(message = "日志id列表，logIds不能为null且数组列表长度必须大于0")
            @RequestParam("logIds") List<Long> logIds) {
        logService.delete(logIds);
        return DbbResponse.success();
    }

    /**
     * @param logType 日志类型
     * @param dto     日志导出传参
     * @description: 日志导出
     * @author: 白剑民
     * @date: 2023-05-29 17:12:32
     * @return: DbbResponse<Void>
     * @version: 1.0
     */
    @Operation(summary = "日志导出")
    @PostMapping("/export")
    public void export(@PathVariable("logType") LogTypeEnum logType, @Valid @RequestBody LogSearchDTO dto) {
        dto.setLogType(logType);
        logService.exportExcel(dto);
    }

    /**
     * @param logType 日志类型
     * @description: 清空日志
     * @author: 白剑民
     * @date: 2023-05-29 17:12:51
     * @return: DbbResponse<?>
     * @version: 1.0
     */
    @Operation(summary = "清空日志")
    @DeleteMapping("/clean")
    public DbbResponse<?> clean(@PathVariable("logType") LogTypeEnum logType) {
        logService.clean(logType);
        return DbbResponse.success();
    }

}
