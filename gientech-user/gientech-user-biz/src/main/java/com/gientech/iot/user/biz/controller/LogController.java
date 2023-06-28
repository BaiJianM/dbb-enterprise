package com.gientech.iot.user.biz.controller;

import com.gientech.iot.log.core.enums.LogTypeEnum;
import com.gientech.iot.user.api.pojo.dto.LogSearchDTO;
import com.gientech.iot.user.biz.service.LogService;
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
     * @author: 王强
     * @date: 2023-05-29 17:11:23
     * @return: GientechResponse<PageResultVO < LogSearchResultVO>>
     * @version: 1.0
     */
    @Operation(summary = "日志搜索")
    @PostMapping("/search")
    public GientechResponse<PageResultVO<?>> search(@PathVariable("logType") LogTypeEnum logType,
                                                    @Valid @RequestBody LogSearchDTO dto) {
        dto.setLogType(logType);
        return GientechResponse.success(logService.search(dto));
    }

    /**
     * @param logIds 日志id
     * @description: 日志删除
     * @author: 王强
     * @date: 2023-05-29 17:12:24
     * @return: GientechResponse<Void>
     * @version: 1.0
     */
    @Operation(summary = "日志删除")
    @Parameter(name = "logIds", description = "日志id列表", required = true)
    @DeleteMapping
    public GientechResponse<Void> delete(
            @NotEmpty(message = "日志id列表，logIds不能为null且数组列表长度必须大于0")
            @RequestParam("logIds") List<Long> logIds) {
        logService.delete(logIds);
        return GientechResponse.success();
    }

    /**
     * @param logType 日志类型
     * @param dto     日志导出传参
     * @description: 日志导出
     * @author: 王强
     * @date: 2023-05-29 17:12:32
     * @return: GientechResponse<Void>
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
     * @author: 王强
     * @date: 2023-05-29 17:12:51
     * @return: GientechResponse<?>
     * @version: 1.0
     */
    @Operation(summary = "清空日志")
    @DeleteMapping("/clean")
    public GientechResponse<?> clean(@PathVariable("logType") LogTypeEnum logType) {
        logService.clean(logType);
        return GientechResponse.success();
    }

}
