package top.dabaibai.thread.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 线程执行任务对象
 * @author: 白剑民
 * @dateTime: 2022/6/29 09:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    /**
     * 数据索引
     */
    private Integer index;
    /**
     * 数据实体
     */
    private Object data;
}
