package com.gientech.iot.user.api.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 数据字典信息
 * @author: 白剑民
 * @dateTime: 2023/5/20 21:14
 */
@Data
@Schema(description = "数据字典信息")
public class DictInfoVO {

    @Schema(description = "数据字典主键id")
    private Long dictId;

    @Schema(description = "上级数据字典id")
    private Long dictParentId;

    @Schema(description = "数据字典类型")
    private String dictType;

    @Schema(description = "数据字典代码")
    private String dictCode;

    @Schema(description = "数据字典名称")
    private String dictName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "数据字典等级")
    private Integer level;
}
