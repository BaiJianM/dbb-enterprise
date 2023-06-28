package com.gientech.iot.gateway.manager.authentication.sms;


import com.gientech.iot.gateway.bean.User;
import com.gientech.iot.gateway.constant.GatewayConstants;
import com.gientech.iot.gateway.manager.authentication.BaseAuthenticationManager;
import com.gientech.iot.gateway.service.UserService;
import com.gientech.iot.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.gientech.iot.gateway.constant.GatewayConstants.AuthErrorConstant.USERNAME_NOT_FOUND;

/**
 * @description: 短信登录会通过该处理类校验账号密码及账号信息
 * @author: 白剑民
 * @dateTime: 2022-10-17 21:46:28
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SmsReactiveAuthenticationManager implements BaseAuthenticationManager<SmsAuthentication> {

    private final UserService userService;
    private final RedisUtils redisUtils;

    @Override
    public Mono<Authentication> authenticate(SmsAuthentication authentication) {
        log.info("=====================手机号登录认证=====================");
        // 获取输入的手机号
        String phone = authentication.getPhone();
        // 获取输入的验证码
        String code = authentication.getSmsCode();
        // 获取子系统ID
        Long systemId = authentication.getSystemId();
        // TODO 校验验证码
        String authCodeKey = String.format(GatewayConstants.AuthConstant.PHONE_FORMAT, phone);
        if (!redisUtils.get(authCodeKey).orElse("").equals(code)) {
            return Mono.error(new BadCredentialsException(GatewayConstants.AuthErrorConstant.SMS_CODE_ERROR));
        }
        // 根据手机号和子系统ID获取用户信息
        User user = userService.loadUserByPhone(phone, systemId);
        if (user == null) {
            return Mono.error(new BadCredentialsException(USERNAME_NOT_FOUND));
        }
        // 判断用户是否禁用
        else if (!user.isEnabled()) {
            return Mono.error(new DisabledException(GatewayConstants.AuthErrorConstant.ACCOUNT_DISABLED));
        }
        // 判断用户是否锁定
        else if (!user.isAccountNonLocked()) {
            return Mono.error(new LockedException(GatewayConstants.AuthErrorConstant.ACCOUNT_LOCKED));
        }
        // 判断账号是否过期
        else if (!user.isAccountNonExpired()) {
            return Mono.error(new AccountExpiredException(GatewayConstants.AuthErrorConstant.ACCOUNT_EXPIRED));
        }
        // 判断凭证是否过期
        else if (!user.isCredentialsNonExpired()) {
            return Mono.error(new CredentialsExpiredException(GatewayConstants.AuthErrorConstant.LOGIN_EXPIRED));
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(user, code, user.getAuthorities());
        // WebFlux方式默认没有放到context中，需要手动放入
        SecurityContextHolder.getContext().setAuthentication(auth);
        return Mono.just(auth);
    }
}
