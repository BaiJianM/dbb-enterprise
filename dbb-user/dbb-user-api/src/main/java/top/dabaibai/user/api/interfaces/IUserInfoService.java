package top.dabaibai.user.api.interfaces;

import top.dabaibai.user.api.pojo.vo.UserLoginVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "IUserInfoService", name = "dbb-user", path = "/user")
public interface IUserInfoService {

    /**
     * @param username 用户名
     * @description: 根据用户名查询用户信息
     * @author: 白剑民
     * @date: 2022-10-24 13:35:28
     * @return: LoginVO
     * @version: 1.0
     */
    @GetMapping(value = "/user/loginByUsername")
    UserLoginVO findUserByUsername(@RequestParam("username") String username, @RequestParam("systemId") Long systemId);

    /**
     * @param phone 手机号
     * @description: 根据手机号查询用户信息
     * @author: 白剑民
     * @date: 2022-10-24 13:35:28
     * @return: LoginVO
     * @version: 1.0
     */
    @GetMapping(value = "/user/loginByPhone")
    UserLoginVO findUserByPhone(@RequestParam("phone") String phone, @RequestParam("systemId") Long systemId);

    /**
     * @param authCode  验证码code
     * @param uuid      验证码uuid
     * @description: 校验验证码
     * @author: 白剑民
     * @date: 2023-04-21 17:50:20
     * @return: boolean
     * @version: 1.0
     */
    @GetMapping(value = "/authCode/verify")
    boolean verify(@RequestParam("authCode") String authCode, @RequestParam("uuid") String uuid);

    /**
     * @description: 登录成功
     * @author: 白剑民
     * @date: 2023-05-29 12:16:24
     * @return: void
     * @version: 1.0
     */
    @GetMapping(value = "/user/loginSuccess")
    void loginSuccess(@RequestHeader MultiValueMap<String, String> headers);

    /**
     * @description: 注销成功
     * @author: 白剑民
     * @date: 2023-05-29 12:16:25
     * @return: void
     * @version: 1.0
     */
    @GetMapping(value = "/user/logoutSuccess")
    void logoutSuccess(@RequestHeader MultiValueMap<String, String> headers);

}
