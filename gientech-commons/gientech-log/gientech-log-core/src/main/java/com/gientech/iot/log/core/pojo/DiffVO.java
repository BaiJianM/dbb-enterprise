package com.gientech.iot.log.core.pojo;

import lombok.Data;

import java.util.List;

/**
 * @description: 发生变化的数据信息
 * @author: 王强
 * @dateTime: 2022-09-01 17:57:43
 */
@Data
public class DiffVO {

    /**
     * 实体类名
     */
    private String oldClassName;

    /**
     * 实体类别名
     */
    private String oldClassAlias;

    /**
     * 实体类名
     */
    private String newClassName;

    /**
     * 实体类别名
     */
    private String newClassAlias;

    /**
     * 发生变化的字段信息列表
     */
    private List<DiffFieldVO> diffFieldVOList;
}
