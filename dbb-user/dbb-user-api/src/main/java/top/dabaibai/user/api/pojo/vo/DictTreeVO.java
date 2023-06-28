package top.dabaibai.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.dabaibai.core.pojo.vo.BaseTreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description: 数据字典树列表VO
 * @author: 白剑民
 * @dateTime: 2023/5/22 13:42
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Schema(description = "数据字典树列表")
public class DictTreeVO extends BaseTreeVO {

    private static final long serialVersionUID = -174319229475295487L;
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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "创建人姓名")
    private String createUserName;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "修改人姓名")
    private String updateUserName;
}
