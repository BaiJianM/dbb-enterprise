package com.gientech.iot.demo.api.pojo.dto;

import com.gientech.iot.log.annotations.LogDiff;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 操作日志测试入参
 * @author: 白剑民
 * @dateTime: 2023/5/6 14:56
 */
@Slf4j
@Data
@Schema(description = "操作日志入参DTO")
public class TestOperationLogDTO {

    @LogDiff(alias = "姓名")
    @Schema(description = "姓名")
    private String name;

    @LogDiff(alias = "年龄")
    @Schema(description = "年龄")
    private Integer age;

}
