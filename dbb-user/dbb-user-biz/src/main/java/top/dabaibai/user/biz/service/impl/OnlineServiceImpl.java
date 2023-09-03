package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dabaibai.core.pojo.vo.BaseUserInfoVO;
import top.dabaibai.core.utils.DateUtils;
import top.dabaibai.core.utils.UserInfoUtils;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.user.api.enums.LoginTypeEnum;
import top.dabaibai.user.api.pojo.dto.OnlineSearchDTO;
import top.dabaibai.user.api.pojo.vo.OnlineSearchResultVO;
import top.dabaibai.user.biz.entity.SysLog;
import top.dabaibai.user.biz.entity.SysSystem;
import top.dabaibai.user.biz.service.LogService;
import top.dabaibai.user.biz.service.OnlineService;
import top.dabaibai.user.biz.service.SystemService;
import top.dabaibai.web.commons.model.PageResultVO;

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
