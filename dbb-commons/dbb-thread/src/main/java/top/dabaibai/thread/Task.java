package top.dabaibai.thread;

import lombok.Data;

/**
 * @description: 线程执行任务对象
 * @author: 白剑民
 * @dateTime: 2022/6/29 09:54
 */
@Data
public class Task {
    /**
     * 对象索引
     */
    private Integer index;
    /**
     * 数据实体
     */
    private Object data;
}
