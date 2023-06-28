package com.gientech.iot.gateway.manager.authorization;

import com.gientech.iot.gateway.bean.UserGrantedAuthority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @description: 用户通过身份验证后，会通过该处理类校验是否有该资源的访问权限
 * @author: 王强
 * @dateTime: 2022-10-17 21:47:52
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        log.info("=====================资源鉴权=====================");
        ServerHttpRequest request = object.getExchange().getRequest();
        String uri = request.getPath().pathWithinApplication().value();
        return authentication
                // 检查是否认证通过
                .filter(new Predicate<Authentication>() {
                    @Override
                    public boolean test(Authentication t) {
                        return t.isAuthenticated();
                    }
                })
                // 获取用户拥有的权限集合(这里是uris)
                .flatMapIterable(new Function<Authentication, Iterable<? extends GrantedAuthority>>() {
                    @Override
                    public Iterable<? extends GrantedAuthority> apply(Authentication t) {
                        // 当用户无任何资源权限时，默认赋予/gientech/login权限
                        return CollectionUtils.isEmpty(t.getAuthorities()) ?
                                Collections.singletonList(new UserGrantedAuthority("/pta/login")) : t.getAuthorities();
                    }
                })
                // 这里放回用户拥有的权限代码(uri)会用于下面的的匹配,
                // 如果该用户有10个权限代码,那么apply(GrantedAuthority t)会被调用10次
                .map(new Function<GrantedAuthority, String>() {
                    @Override
                    public String apply(GrantedAuthority t) {
                        return t.getAuthority();
                    }
                })
                // 进行匹配,如果该用户有10个权限代码,那么apply(GrantedAuthority t)会被调用10次,直到匹配上为止
                .any(new Predicate<String>() {
                    @Override
                    public boolean test(String pattern) {
                        // TODO 暂不校验uri权限
//                        if (antPathMatcher.match(pattern, uri)) {
//                            return true;
//                        }
//                        return false;
                        return true;
                    }
                }).map(new Function<Boolean, AuthorizationDecision>() {
                    @Override
                    public AuthorizationDecision apply(Boolean t) {
                        return new AuthorizationDecision(t);
                    }
                }).defaultIfEmpty(new AuthorizationDecision(false));
    }
}
