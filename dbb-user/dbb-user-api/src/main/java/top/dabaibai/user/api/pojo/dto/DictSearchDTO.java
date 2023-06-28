package top.dabaibai.user.api.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.dabaibai.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description: 数据字典搜索传参
 * @author: 白剑民
 * @dateTime: 2023/5/2 20:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据字典搜索传参")
public class DictSearchDTO extends PageDTO {

    @Schema(description = "数据字典类型")
    private String dictType;

    @Schema(description = "数据字典代码")
    private String dictCode;

    @Schema(description = "数据字典名称")
    private String dictName;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "数据字典等级")
    private Integer level;

    @Schema(description = "创建时间起始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;

    @Schema(description = "创建时间截止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endCreateTime;

}
