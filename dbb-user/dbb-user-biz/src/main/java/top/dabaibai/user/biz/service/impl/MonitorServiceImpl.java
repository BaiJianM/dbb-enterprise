package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import top.dabaibai.log.core.enums.LogTypeEnum;
import top.dabaibai.user.api.pojo.vo.SystemStatisticsVO;
import top.dabaibai.user.biz.entity.SysLog;
import top.dabaibai.user.biz.entity.SysSystem;
import top.dabaibai.user.biz.mapper.LogMapper;
import top.dabaibai.user.biz.mapper.SystemMapper;
import top.dabaibai.user.biz.service.MonitorService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static top.dabaibai.user.api.pojo.vo.SystemStatisticsVO.CountInfo;
import static top.dabaibai.user.api.pojo.vo.SystemStatisticsVO.HealthyInfo;

/**
 * @description: 系统监控实现类
 * @author: 白剑民
 * @dateTime: 2023/5/29 16:10
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MonitorServiceImpl implements MonitorService {

    private final LogMapper logMapper;

    private final SystemMapper systemMapper;

    private final DiscoveryClient discoveryClient;

    @Override
    public List<SystemStatisticsVO> systemHealthy() {
        // 指定目标日期
        LocalDate date = LocalDate.now();
        // 近7日
        int range = 7;
        LocalDate[] dateArray = new LocalDate[range];
        for (int i = 0; i < range; i++) {
            dateArray[i] = date.minusDays(i);
        }
        // 查出近7日的登录日志
        List<SysLog> logList =
                logMapper.getLogByParams(Collections.singletonList("100001"),
                        LogTypeEnum.LOGIN_LOG.getCode(),
                        date.minusDays(range), LocalDate.now().plusDays(1));
        // 获取所有子系统
        LambdaQueryWrapper<SysSystem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysSystem::getIsEnable, true);
        wrapper.eq(SysSystem::getIsDelete, false);
        List<SysSystem> systemList = systemMapper.selectList(wrapper);
        List<SystemStatisticsVO> result = new ArrayList<>();
        // 遍历子系统列表，预设置子系统统计信息
        systemList.forEach(s -> {
            SystemStatisticsVO ss = new SystemStatisticsVO();
            this.getHealthy(s, ss);
            result.add(ss);
        });
        // 遍历日志数据，以子系统分组
        Map<Long, List<SysLog>> logMap = logList.stream().collect(Collectors.groupingBy(SysLog::getSystemId));
        // 遍历结果集，统计登录信息
        result.forEach(r -> {
            List<CountInfo> countInfoList = new ArrayList<>();
            logMap.getOrDefault(r.getSystemId(), new ArrayList<>())
                    .stream()
                    // 按日期分组
                    .collect(Collectors.groupingBy(log -> log.getCreateTime().toLocalDate()))
                    // 遍历每个日期的登录日志，并统计对应日期当天登录情况
                    .forEach((k1, v1) -> {
                        CountInfo countInfo = new CountInfo();
                        countInfo.setDate(k1);
                        countInfo.setCount((long) v1.size());
                        countInfoList.add(countInfo);
                    });
            // 一个子系统遍历结束后，如果遍历出的数据没有涵盖所有指定日期，则空白日期统计补0
            if (countInfoList.size() != range) {
                // 取统计到的子系统日期列表
                List<LocalDate> dateList = countInfoList.stream()
                        .map(CountInfo::getDate).collect(Collectors.toList());
                // 与指定日期范围取差集
                Set<LocalDate> diffSet = new HashSet<>(Arrays.asList(dateArray));
                dateList.forEach(diffSet::remove);
                diffSet.forEach(d -> {
                    CountInfo countInfo = new CountInfo();
                    countInfo.setDate(d);
                    countInfo.setCount(0L);
                    countInfoList.add(countInfo);
                });
            }
            r.setCountInfoList(countInfoList);
        });
        return result;
    }

    /**
     * @param s  子系统实例
     * @param ss 子系统统计信息
     * @description: 根据nacos服务名获取该服务下的所有节点状态
     * @author: 白剑民
     * @date: 2023-05-30 14:21:26
     * @version: 1.0
     */
    private void getHealthy(SysSystem s, SystemStatisticsVO ss) {
        List<HealthyInfo> healthyInfoList = new ArrayList<>();
        String serviceId = s.getServiceId();
        if (StringUtils.isNotBlank(serviceId)) {
            // 从nacos中获取指定服务名的所有实例节点
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            // 遍历所有实例节点
            for (int i = 0; i < instances.size(); i++) {
                ServiceInstance instance = instances.get(i);
                HealthyInfo healthyInfo = new HealthyInfo();
                String prefix = "node-";
                // 节点名
                healthyInfo.setNode(prefix + (i + 1));
                // 获取健康状态
                healthyInfo.setIsOnline(Boolean.parseBoolean(instance.getMetadata().get("nacos.healthy")));
                healthyInfoList.add(healthyInfo);
            }
        }
        ss.setSystemId(s.getId());
        ss.setSystemName(s.getSystemName());
        ss.setHealthyInfoList(healthyInfoList);
    }
}
