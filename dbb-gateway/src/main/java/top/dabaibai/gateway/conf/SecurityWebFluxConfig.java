package top.dabaibai.gateway.conf;

import feign.Feign;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.dabaibai.gateway.filter.authentication.LoginAuthenticationWebFilter;
import top.dabaibai.gateway.handler.*;
import top.dabaibai.gateway.manager.authentication.account.AccountAuthenticationConverter;
import top.dabaibai.gateway.manager.authentication.account.AccountReactiveAuthenticationManager;
import top.dabaibai.gateway.manager.authentication.sms.SmsAuthenticationConverter;
import top.dabaibai.gateway.manager.authentication.sms.SmsReactiveAuthenticationManager;
import top.dabaibai.gateway.manager.authorization.CustomReactiveAuthorizationManager;

import java.util.concurrent.TimeUnit;

/**
 * @description: SpringSecurity配置类
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:02:25
 */
@Component
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class SecurityWebFluxConfig {

    /**
     * 用户名登录认证服务
     */
    private final AccountReactiveAuthenticationManager usernameAuthenticationManager;

    /**
     * 用户名登录参数转换
     */
    private final AccountAuthenticationConverter usernameAuthenticationConverter;

    /**
     * 手机号认证服务
     */
    private final SmsReactiveAuthenticationManager phoneReactiveAuthenticationManager;

    /**
     * 手机号登录参数转换
     */
    private final SmsAuthenticationConverter phoneAuthenticationConverter;

    /**
     * 身份验证成功处理程序
     */
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 身份验证失败处理程序
     */
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 自定义的鉴权服务，通过鉴权的才能继续访问某个请求
     */
    private final CustomReactiveAuthorizationManager authorizationManager;

    /**
     * 未登录访问资源时的处理类，若无此处理类，前端页面会弹出登录窗口
     */
    private final CustomHttpBasicServerAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 无权限访问被拒绝时的自定义处理器。如不自己处理，默认返回403错误
     */
    private final CustomServerAccessDeniedHandler accessDeniedHandler;

    /**
     * 登出成功时调用的自定义处理类
     */
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    /**
     * security的鉴权白名单
     */
    private final PermitAllUrlProperties permitAllUrlProperties;

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = http.authorizeExchange();
        // 无需进行权限过滤的请求路径
        String[] ignoreUrls = permitAllUrlProperties.getIgnoreUrls();
        if (ignoreUrls.length > 0) {
            authorizeExchangeSpec.pathMatchers(ignoreUrls).permitAll();
        }
        authorizeExchangeSpec
                // option 请求默认放行
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                // 自定义的鉴权服务，通过鉴权的才能继续访问某个请求
                .and().authorizeExchange().pathMatchers("/**").access(authorizationManager)
                .anyExchange().authenticated()
                // 开启httpBasic认证
                .and().httpBasic()
                // 表单登录
//                .and().formLogin()
                // 未登录访问资源时的处理类，若无此处理类，前端页面会弹出登录窗口
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                // 访问被拒绝时自定义处理器
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                // 必须支持跨域
                .and().csrf().disable()
                // 登出接口, 默认 /logout
                .logout().logoutUrl("/logout")
                // 登出成功
                .logoutSuccessHandler(logoutSuccessHandler);
        // 账号登录认证过滤器
        http.addFilterAt(accountAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        // 短信登录认证过滤器
        http.addFilterAt(smsAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    @Bean("accountAuthenticationFilter")
    public LoginAuthenticationWebFilter accountAuthenticationFilter() {
        LoginAuthenticationWebFilter filter = new LoginAuthenticationWebFilter(usernameAuthenticationManager);
        filter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login"));
        filter.setServerAuthenticationConverter(usernameAuthenticationConverter);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Bean("smsAuthenticationFilter")
    public LoginAuthenticationWebFilter smsAuthenticationFilter() {
        LoginAuthenticationWebFilter filter = new LoginAuthenticationWebFilter(phoneReactiveAuthenticationManager);
        filter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/sms/login"));
        filter.setServerAuthenticationConverter(phoneAuthenticationConverter);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    /**
     * @description: spring security密码加密策略
     * @author: 白剑民
     * @date: 2023-05-23 09:41:33
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @version: 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Feign.Builder feignBuilder(Encoder encoder, Decoder decoder) {
        return HystrixFeign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .options(new Request.Options(3, TimeUnit.SECONDS, 3, TimeUnit.SECONDS, false))
                .contract(new SpringMvcContract());
    }
}