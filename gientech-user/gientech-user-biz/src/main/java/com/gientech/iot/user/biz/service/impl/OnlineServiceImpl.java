package com.gientech.iot.user.biz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gientech.iot.core.pojo.vo.BaseUserInfoVO;
import com.gientech.iot.core.utils.DateUtils;
import com.gientech.iot.core.utils.UserInfoUtils;
import com.gientech.iot.redis.utils.RedisUtils;
import com.gientech.iot.user.api.enums.LoginTypeEnum;
import com.gientech.iot.user.api.pojo.dto.OnlineSearchDTO;
import com.gientech.iot.user.api.pojo.vo.OnlineSearchResultVO;
import com.gientech.iot.user.biz.entity.SysLog;
import com.gientech.iot.user.biz.entity.SysSystem;
import com.gientech.iot.user.biz.service.LogService;
import com.gientech.iot.user.biz.service.OnlineService;
import com.gientech.iot.user.biz.service.SystemService;
import com.gientech.iot.web.commons.model.PageResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 在线用户信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OnlineServiceImpl implements OnlineService {

    private final LogService logService;

    private final RedisUtils redisUtils;

    private final SystemService systemService;

    @Override
    public PageResultVO<OnlineSearchResultVO> page(OnlineSearchDTO dto) {
        return logService.onlineUserPage(dto);
    }

    @Override
    public void delete(List<Long> onlineIds) {
        List<SysSystem> systemList = systemService.list();
        Map<Long, String> systemMap = systemList.stream().collect(Collectors.toMap(SysSystem::getId, SysSystem::getSystemName));
        BaseUserInfoVO user = UserInfoUtils.getUserInfo();
        String str = "【%s】被【%s】强制退出【%s】系统，退出时间【%s】";
        List<SysLog> sysLogs = logService.listByIds(onlineIds);
        for (SysLog log : sysLogs) {
            log.setId(IdWorker.getId());
            log.setEvent(LoginTypeEnum.LOGOUT.getCode());
            log.setMsg(String.format(str, log.getCreateUserName(), user.getRealName(), systemMap.get(log.getSystemId()), DateUtils.now()));
        }
        logService.saveBatch(sysLogs);
        List<String> collect = sysLogs.stream().map(p -> String.format("token:%s", p.getCreateUserId())).collect(Collectors.toList());
        redisUtils.delete(collect);
    }
}
