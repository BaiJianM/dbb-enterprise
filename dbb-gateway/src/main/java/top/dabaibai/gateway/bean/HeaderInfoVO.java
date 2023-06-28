package top.dabaibai.gateway.bean;

import lombok.Data;

/**
 * @description: 网关登录认证通过后向请求头中存入，用于用户信息本地缓存读取
 * @author: 白剑民
 * @dateTime: 2023/5/24 14:53
 */
@Data
public class HeaderInfoVO {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 登录的应用子系统id
     */
    private Long systemId;
    /**
     * 登录的应用子系统名称
     */
    private String systemName;
}
