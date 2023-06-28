package com.gientech.iot.core.utils.excel;

import com.alibaba.excel.write.handler.CellWriteHandler;

/**
 * @description: 自定义导出Excel处理器
 * @author: 白剑民
 * @dateTime: 2023-03-15 12:05:16
 */
public interface CustomCellWriteHandler extends CellWriteHandler {
    /**
     * @param cellIndex 列下标
     * @description: 将列下标转换为列号, 例（0 转成 A）
     * @author: 白剑民
     * @date: 2023-03-15 11:24:37
     * @return: String
     * @version: 1.0
     */
    default String convertCellIndexToCellStr(final int cellIndex) {
        String cellStr = "";
        int a = cellIndex / 26;
        int b = cellIndex % 26;
        if (a > 0) {
            cellStr += (char) (a + 64);
        }
        cellStr += (char) (b + 65);
        return cellStr;
    }
}
