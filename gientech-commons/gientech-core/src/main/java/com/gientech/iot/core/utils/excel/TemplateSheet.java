package com.gientech.iot.core.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 多sheet模板导出
 * @author: 王强
 * @dateTime: 2023-03-16 14:44:04
 */
@Data
@AllArgsConstructor
public class TemplateSheet {

    /**
     * sheet下标
     */
    private int sheetIndex;
    /**
     * sheet名称
     */
    private String sheetName;
}
