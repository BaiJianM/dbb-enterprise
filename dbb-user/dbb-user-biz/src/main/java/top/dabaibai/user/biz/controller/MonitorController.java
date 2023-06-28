package top.dabaibai.user.biz.controller;

import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.user.api.pojo.UserConstants;
import top.dabaibai.user.api.pojo.vo.RedisCacheVO;
import top.dabaibai.user.api.pojo.vo.SystemStatisticsVO;
import top.dabaibai.user.biz.service.MonitorService;
import top.dabaibai.web.commons.http.DbbResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: 系统监控控制层
 * @author: 白剑民
 * @dateTime: 2023/4/27 15:09
 */
@Slf4j
@Tag(name = "系统监控相关接口")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MonitorController {

    private final RedisUtils redisUtils;

    private final MonitorService monitorService;

    /**
     * @description: 获取Redis服务信息
     * @author: 白剑民
     * @date: 2023-04-27 15:27:36
     * @return: top.dabaibai.core.global.response.DbbResponse<java.util.Map < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    @GetMapping("/cache")
    public DbbResponse<Map<String, Object>> getInfo() {
        Properties info = (Properties) redisUtils.execute(RedisServerCommands::info);
        Properties commandStats = (Properties) redisUtils.execute(connection -> connection.info("commandstats"));
        Object dbSize = redisUtils.execute(RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        assert commandStats != null;
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return DbbResponse.success(result);
    }

    /**
     * @description: 获取系统定义的缓存前缀名列表
     * @author: 白剑民
     * @date: 2023-04-27 15:27:50
     * @return: top.dabaibai.core.global.response.DbbResponse<java.util.List < top.dabaibai.user.api.entity.vo.RedisCacheVO>>
     * @version: 1.0
     */
    @GetMapping("/getNames")
    public DbbResponse<List<RedisCacheVO>> cache() {
        return DbbResponse.success(UserConstants.RedisCache.getCaches());
    }

    /**
     * @param cacheName 缓存前缀名
     * @description: 根据缓存前缀名获取其下所有的Key列表
     * @author: 白剑民
     * @date: 2023-04-27 15:28:42
     * @return: top.dabaibai.core.global.response.DbbResponse<java.util.Set < java.lang.String>>
     * @version: 1.0
     */
    @GetMapping("/getKeys/{cacheName}")
    public DbbResponse<Set<String>> getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisUtils.keys(cacheName + "*").orElse(new HashSet<>());
        return DbbResponse.success(cacheKeys);
    }

    /**
     * @param cacheName 缓存前缀名
     * @param cacheKey  缓存key
     * @description: 根据前缀+key获取缓存内容并封装内容返回
     * @author: 白剑民
     * @date: 2023-04-27 15:29:09
     * @return: top.dabaibai.core.global.response.DbbResponse<top.dabaibai.user.api.entity.vo.RedisCacheVO>
     * @version: 1.0
     */
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public DbbResponse<RedisCacheVO> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = redisUtils.get(cacheKey).orElse(new Object()).toString();
        RedisCacheVO sysCache = new RedisCacheVO(cacheName, cacheKey, cacheValue);
        return DbbResponse.success(sysCache);
    }

    /**
     * @param cacheName 缓存前缀名
     * @description: 根据缓存前缀，删除其下所有缓存
     * @author: 白剑民
     * @date: 2023-04-27 15:39:26
     * @return: top.dabaibai.core.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @DeleteMapping("/clearCacheName/{cacheName}")
    public DbbResponse<Void> clearCacheName(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisUtils.keys(cacheName + "*").orElse(new HashSet<>());
        redisUtils.delete(cacheKeys);
        return DbbResponse.success();
    }

    /**
     * @param cacheKey 缓存key
     * @description: 删除指定key
     * @author: 白剑民
     * @date: 2023-04-27 16:56:29
     * @return: top.dabaibai.core.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public DbbResponse<Void> clearCacheKey(@PathVariable String cacheKey) {
        redisUtils.delete(cacheKey);
        return DbbResponse.success();
    }

    /**
     * @description: 统计子系统信息
     * @author: 白剑民
     * @date: 2023-05-29 19:10:43
     * @return: top.dabaibai.core.global.response.DbbResponse<top.dabaibai.user.api.pojo.vo.SystemStatisticsVO>
     * @version: 1.0
     */
    @Operation(summary = "统计子系统信息")
    @GetMapping("/statistics/info")
    public DbbResponse<List<SystemStatisticsVO>> systemHealthy() {
        return DbbResponse.success(monitorService.systemHealthy());
    }
}
