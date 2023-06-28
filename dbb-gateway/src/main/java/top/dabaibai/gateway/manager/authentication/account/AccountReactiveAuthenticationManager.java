package top.dabaibai.gateway.manager.authentication.account;

import top.dabaibai.core.utils.DateUtils;
import top.dabaibai.gateway.bean.User;
import top.dabaibai.gateway.constant.GatewayConstants;
import top.dabaibai.gateway.manager.authentication.BaseAuthenticationManager;
import top.dabaibai.gateway.service.UserService;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.thread.DbbThreadPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 账号登录会通过该处理类校验账号密码及账号信息
 * @author: 白剑民
 * @dateTime: 2022-10-17 21:46:28
 */
@Slf4j
@Primary
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AccountReactiveAuthenticationManager implements BaseAuthenticationManager<AccountAuthentication> {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;

    @Override
    public Mono<Authentication> authenticate(AccountAuthentication authentication) {
        log.info("=====================用户名登录认证=====================");
        // 获取输入的用户名
        String username = authentication.getUsername();
        // 获取输入的密码
        String password = authentication.getPassword();
        // 获取验证码
        String authCode = authentication.getCode();
        // 获取验证码uuid
        String uuid = authentication.getUuid();
        // 获取应用子系统id
        Long systemId = authentication.getSystemId();
        // 校验验证码
        if (!userService.verify(authCode, uuid)) {
            return Mono.error(new BadCredentialsException(GatewayConstants.AuthErrorConstant.AUTH_CODE_ERROR));
        }
        // 根据用户名和子系统ID获取用户信息
        User user = userService.loadUserByUsername(username, systemId);
        if (user == null) {
            return Mono.error(new BadCredentialsException(GatewayConstants.AuthErrorConstant.USERNAME_NOT_FOUND));
        }
        // 先判断用户账号是否因输错账号密码次数已经到冻结条件
        String freezeKey = String.format(GatewayConstants.AuthConstant.ACCOUNT_FREEZE_PREFIX, user.getUserId());
        if (redisUtils.hasKey(freezeKey).orElse(false)) {
            Long keyExpire = redisUtils.getExpire(freezeKey).orElse(0L);
            // 计算解冻时间，当前时间+剩余的key过期时间
            String unFreezeTime = DateUtils.parseTime(
                    LocalDateTime.now().plusSeconds(keyExpire),
                    DateUtils.TimeFormat.LONG_DATE_PATTERN_LINE);
            String errorMsg = String.format(GatewayConstants.AuthErrorConstant.ACCOUNT_FREEZE, user.getRetryNum(), unFreezeTime);
            return Mono.error(new BadCredentialsException(errorMsg));
        }
        // 用户账号密码输错后的重试计数缓存
        String retryKey = String.format(GatewayConstants.AuthConstant.RETRY_KEY_PREFIX, user.getUserId());
        // 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 如果已经存在重试计数缓存，就取出其计数值，并判断是否需要返回冻结
            AtomicReference<String> notice = new AtomicReference<>(String.format(GatewayConstants.AuthErrorConstant.PASSWORD_ERROR, user.getRetryNum()));
            if (redisUtils.hasKey(retryKey).orElse(false)) {
                redisUtils.get(retryKey).ifPresent(val -> {
                    // 如果已达到剩余1次计数就冻结
                    int count = Integer.parseInt(val.toString());
                    if (count == 1) {
                        // 冻结
                        redisUtils.setEx(freezeKey, user.getUserId(), user.getFreezeTime(), TimeUnit.HOURS);
                        // 删除计数缓存
                        redisUtils.delete(retryKey);
                        // 计算解冻时间
                        String unFreezeTime = DateUtils.parseTime(
                                LocalDateTime.now().plusHours(user.getFreezeTime()),
                                DateUtils.TimeFormat.LONG_DATE_PATTERN_LINE);
                        notice.set(String.format(GatewayConstants.AuthErrorConstant.ACCOUNT_FREEZE, user.getRetryNum(), unFreezeTime));
                    } else {
                        // 否则减一后再次缓存
                        count--;
                        redisUtils.set(retryKey, count);
                        notice.set(String.format(GatewayConstants.AuthErrorConstant.PASSWORD_ERROR, count));
                    }
                });
            } else {
                // 初始化重试计数缓存
                redisUtils.set(retryKey, user.getRetryNum());
            }
            return Mono.error(new BadCredentialsException(notice.get()));
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
        // 判断是否有系统权限
        else if (CollectionUtils.isEmpty(user.getAuthorities())) {
            return Mono.error(new CredentialsExpiredException(GatewayConstants.AuthErrorConstant.SYSTEM_FORBIDDEN));
        }
        // 异步处理成功认证后可能存在的登录重试计数缓存
        DbbThreadPool.initThreadPool().execute(() -> {
            redisUtils.hasKey(retryKey).ifPresent(f -> redisUtils.delete(retryKey));
        });
        Authentication auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        // WebFlux方式默认没有放到context中，需要手动放入
        SecurityContextHolder.getContext().setAuthentication(auth);
        return Mono.just(auth);
    }
}
