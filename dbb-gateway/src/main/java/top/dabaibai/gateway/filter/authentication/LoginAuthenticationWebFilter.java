package top.dabaibai.gateway.filter.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import top.dabaibai.gateway.manager.authentication.BaseAuthentication;
import top.dabaibai.gateway.manager.authentication.BaseAuthenticationConverter;
import top.dabaibai.gateway.manager.authentication.BaseAuthenticationManager;
import top.dabaibai.gateway.manager.authentication.BaseAuthenticationManagerResolver;

import java.util.function.Function;

/**
 * @description: 自定义身份验证web过滤器
 * @author: 白剑民
 * @dateTime: 2022-10-27 10:43:16
 */
@Slf4j
public class LoginAuthenticationWebFilter implements WebFilter {

    private final BaseAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver;

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler = new WebFilterChainServerAuthenticationSuccessHandler();

    private BaseAuthenticationConverter authenticationConverter;

    private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(
            new HttpBasicServerAuthenticationEntryPoint());

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository
            .getInstance();

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();


    public LoginAuthenticationWebFilter(BaseAuthenticationManager<?> authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManagerResolver = (request) -> Mono.just(authenticationManager);
    }

    public LoginAuthenticationWebFilter(
            BaseAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {
        Assert.notNull(authenticationManagerResolver, "authenticationResolverManager cannot be null");
        this.authenticationManagerResolver = authenticationManagerResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.requiresAuthenticationMatcher.matches(exchange).filter((matchResult) -> matchResult.isMatch())
                .flatMap((matchResult) -> this.authenticationConverter.convert(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap((token) -> authenticate(exchange, chain, token))
                .onErrorResume(AuthenticationException.class, (ex) -> this.authenticationFailureHandler
                        .onAuthenticationFailure(new WebFilterExchange(exchange, chain), ex));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, BaseAuthentication token) {
        return this.authenticationManagerResolver.resolve(exchange)
                .flatMap((authenticationManager) -> authenticationManager.authenticate(token))
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(new IllegalStateException("No provider found for " + token.getClass()))))
                .flatMap((authentication) -> onAuthenticationSuccess((Authentication) authentication,
                        new WebFilterExchange(exchange, chain)))
                .doOnError(AuthenticationException.class,
                        (ex) -> log.debug(String.format("Authentication failed: %s", ex)));
    }

    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return this.securityContextRepository.save(exchange, securityContext)
                .then(this.authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    public void setSecurityContextRepository(ServerSecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
    }

    public void setAuthenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Deprecated
    public void setAuthenticationConverter(Function<ServerWebExchange, Mono<BaseAuthentication>> authenticationConverter) {
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        setServerAuthenticationConverter(authenticationConverter::apply);
    }

    public void setServerAuthenticationConverter(BaseAuthenticationConverter authenticationConverter) {
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        this.authenticationConverter = authenticationConverter;
    }

    public void setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        Assert.notNull(requiresAuthenticationMatcher, "requiresAuthenticationMatcher cannot be null");
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }
}
