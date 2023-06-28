package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 数据字典更新
 * @author: 白剑民
 * @dateTime: 2023/5/22 11:02
 */
@Data
@Schema(description = "数据字典更新传参")
public class DictUpdateDTO {

    @NotNull(message = "数据字典主键id，dictId不能为null")
    @Min(value = 1, message = "数据字典主键id，dictId数值必须大于0")
    @Schema(description = "数据字典主键id")
    private Long dictId;

    @Schema(description = "数据字典类型")
    private String dictType;

    @Schema(description = "数据字典名称")
    private String dictName;

    @Schema(description = "备注")
    private String remark;
}
