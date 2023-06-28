package com.gientech.iot.user.biz.service;

import com.gientech.iot.user.api.pojo.vo.SystemStatisticsVO;

import java.util.List;

/**
 * @description: 系统监控接口类
 * @author: 白剑民
 * @dateTime: 2023/05/29 15:03
 */
public interface MonitorService {

    /**
     * @description: 统计子系统信息
     * @author: 白剑民
     * @date: 2023-05-29 19:10:02
     * @return: java.util.List<com.gientech.iot.user.api.pojo.vo.SystemStatisticsVO>
     * @version: 1.0
     */
    List<SystemStatisticsVO> systemHealthy();
}
