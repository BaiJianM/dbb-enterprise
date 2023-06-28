package top.dabaibai.user.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 数据字典创建传参
 * @author: 白剑民
 * @dateTime: 2023/5/4 09:21
 */
@Data
@Schema(description = "创建数据字典传参")
public class DictCreateDTO {

    @Schema(description = "上级字典id")
    private Long dictParentId;

    @NotBlank(message = "数据字典类型，dictType不能为null且字符串长度必须大于0")
    @Schema(description = "数据字典类型")
    private String dictType;

    @NotBlank(message = "数据字典代码，dictCode不能为null且字符串长度必须大于0")
    @Schema(description = "数据字典代码")
    private String dictCode;

    @NotBlank(message = "数据字典名称，dictName不能为null且字符串长度必须大于0")
    @Schema(description = "数据字典名称")
    private String dictName;

    @Schema(description = "备注")
    private String remark;

}
