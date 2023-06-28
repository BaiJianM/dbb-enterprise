package top.dabaibai.gateway.service.impl;

import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.gateway.bean.User;
import top.dabaibai.gateway.service.UserService;
import top.dabaibai.user.api.interfaces.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * @description: 用户信息服务impl
 * @author: 白剑民
 * @dateTime: 2022-10-17 22:11:03
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService {

    private final IUserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public User loadUserByUsername(String username, Long systemId) throws UsernameNotFoundException {
        return BeanConvertUtils.convert(userInfoService.findUserByUsername(username, systemId), User::new).orElse(null);
    }

    @Override
    public User loadUserByPhone(String phone, Long systemId) throws UsernameNotFoundException {
        return BeanConvertUtils.convert(userInfoService.findUserByPhone(phone, systemId), User::new).orElse(null);
    }

    @Override
    public Boolean verify(String authCode, String uuid) throws UsernameNotFoundException {
        return userInfoService.verify(authCode, uuid);
    }

    @Override
    public void loginSuccess(MultiValueMap<String, String> headers) {
        try {
            userInfoService.loginSuccess(headers);
        } catch (Exception ignored) {}
    }

    @Override
    public void logoutSuccess(MultiValueMap<String, String> headers) {
        try {
            userInfoService.logoutSuccess(headers);
        } catch (Exception ignored) {}
    }
}
