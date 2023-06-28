package com.gientech.iot.user.api.pojo.vo;

import com.gientech.iot.core.pojo.vo.BaseTreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 树数据下拉的返回
 * @author: 白剑民
 * @dateTime: 2022-07-29 09:51:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelectVO<T extends BaseTreeVO> implements Serializable {

    private static final long serialVersionUID = 8481964221820200858L;

    @Schema(description = "选中的keys")
    private List<Long> selectedKeys;

    @Schema(description = "树列表（树形结构展示用）")
    private List<T> treeList;

}
