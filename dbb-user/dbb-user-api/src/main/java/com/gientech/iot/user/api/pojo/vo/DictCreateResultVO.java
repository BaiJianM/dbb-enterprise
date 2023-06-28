package com.gientech.iot.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 数据字典创建返回值
 * @author: 白剑民
 * @dateTime: 2023/5/20 21:06
 */
@Data
@Schema(description = "数据字典创建返回值")
public class DictCreateResultVO {
    @Schema(description = "数据字典主键id")
    private Long dictId;
}
