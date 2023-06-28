package top.dabaibai.user.api.pojo.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/27 15:10
 */
@Data
public class RedisCacheVO {
    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存键名
     */
    private String cacheKey;

    /**
     * 缓存内容
     */
    private String cacheValue;

    /**
     * 备注
     */
    private String remark;

    public RedisCacheVO() {

    }

    public RedisCacheVO(String cacheName, String remark) {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public RedisCacheVO(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}
