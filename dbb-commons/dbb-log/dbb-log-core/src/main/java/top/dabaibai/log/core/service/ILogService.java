package top.dabaibai.log.core.service;

import top.dabaibai.log.core.pojo.LogDTO;

/**
 * @description: 日志记录服务
 * @author: 白剑民
 * @dateTime: 2022-09-02 17:31:49
 */
public interface ILogService {

    /**
     * 自定义日志监听
     * @param logDTO 日志传输实体
     */
    void createLog(LogDTO logDTO);

}
