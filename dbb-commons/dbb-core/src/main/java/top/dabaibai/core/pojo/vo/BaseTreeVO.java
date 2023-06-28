package top.dabaibai.core.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 树结构的基类
 * @author: 白剑民
 * @dateTime: 2022-07-29 09:51:33
 */
@Data
public class BaseTreeVO implements Serializable {

    private static final long serialVersionUID = 8481964221820200858L;

    /**
     * 主键id（树形结构展示用）
     */
    private Long id;

    /**
     * 父级id（树形结构展示用）
     */
    private Long parentId;

    @Schema(description = "子集列表（树形结构展示用）")
    private List<? extends BaseTreeVO> children;

    public boolean hasChildren() {
        return children.size() > 0;
    }

}
