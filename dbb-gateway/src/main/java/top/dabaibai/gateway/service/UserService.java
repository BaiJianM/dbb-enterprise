package top.dabaibai.gateway.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;
import top.dabaibai.gateway.bean.User;

/**
 * @description: 用户信息服务
 * @author: 白剑民
 * @dateTime: 2022-10-14 13:12:35
 */
public interface UserService extends UserDetailsService {

    /**
     * @param username 用户名
     * @param systemId 系统标识
     * @description: 根据用户名查询用户信息
     * @author: 白剑民
     * @date: 2023-05-29 12:03:37
     * @return: User
     * @version: 1.0
     */
    User loadUserByUsername(String username, Long systemId) throws UsernameNotFoundException;

    /**
     * @param phone 手机号
     * @param systemId 系统标识
     * @description: 根据手机号查询用户信息
     * @author: 白剑民
     * @date: 2022-10-26 20:01:18
     * @return: UserDetails
     * @version: 1.0
     */
    User loadUserByPhone(String phone, Long systemId) throws UsernameNotFoundException;

    /**
     * @param authCode  验证码code
     * @param uuid      验证码uuid
     * @description: 校验验证码
     * @author: 白剑民
     * @date: 2022-10-26 20:01:18
     * @return: UserDetails
     * @version: 1.0
     */
    Boolean verify(String authCode, String uuid) throws UsernameNotFoundException;

    /**
     * @description: 根据用户名查询用户信息
     * @author: 白剑民
     * @date: 2023-05-29 12:03:37
     * @return: User
     * @version: 1.0
     */
    void loginSuccess(MultiValueMap<String, String> headers);

    /**
     * @description: 根据手机号查询用户信息
     * @author: 白剑民
     * @date: 2022-10-26 20:01:18
     * @return: UserDetails
     * @version: 1.0
     */
    void logoutSuccess(MultiValueMap<String, String> headers);
}
