package top.dabaibai.gateway.filter.authentication;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import top.dabaibai.core.utils.MD5Utils;
import top.dabaibai.gateway.bean.TokenUser;
import top.dabaibai.gateway.constant.GatewayConstants;
import top.dabaibai.gateway.util.TokenUtils;
import top.dabaibai.redis.utils.RedisUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @description: 令牌身份验证过滤器
 * @author: 白剑民
 * @dateTime: 2022-10-26 14:58:06
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TokenAuthenticationFilter implements WebFilter {

    private final RedisUtils redisUtils;

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,@NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求头Authorization信息
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // 判断是否是BearerToken请求
        if (authHeader != null && authHeader.startsWith(GatewayConstants.AuthConstant.BEARER)) {
            // 执行Token请求认证
            log.info("=====================Token认证=====================");
            // 截取Token信息
            String authToken = authHeader.substring(GatewayConstants.AuthConstant.BEARER.length());
            // 校验token, 并获取用户信息
            TokenUser user = TokenUtils.validationToken(authToken);
            if (user == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.error(new CredentialsExpiredException(GatewayConstants.AuthErrorConstant.TOKEN_EXPIRED));
            }
            // 校验redis中的token
            String redisTokenKey = String.format(GatewayConstants.AuthConstant.KEY_FORMAT, user.getUserId());
            if (!MD5Utils.encrypt(authToken).equals(redisUtils.get(redisTokenKey).orElse(""))) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.error(new CredentialsExpiredException(GatewayConstants.AuthErrorConstant.TOKEN_EXPIRED));
            }
            // 验证通过, 刷新token有效时间
            redisUtils.expire(redisTokenKey, GatewayConstants.AuthConstant.TOKEN_EXPIRED, TimeUnit.SECONDS);
            Authentication auth = new UsernamePasswordAuthenticationToken(user, authToken, new ArrayList<>());
            try {
                // 将用户信息放入Header中, URL加密解决中文乱码
                exchange.getRequest().mutate()
                        .header("USER_INFO", URLEncoder.encode(JSON.toJSONString(user), "UTF-8"))
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        } else {
            return chain.filter(exchange);
        }
    }
}
