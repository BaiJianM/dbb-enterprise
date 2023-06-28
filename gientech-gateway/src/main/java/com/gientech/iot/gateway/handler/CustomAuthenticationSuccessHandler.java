package com.gientech.iot.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.core.utils.MD5Utils;
import com.gientech.iot.gateway.bean.AccessTokenInfo;
import com.gientech.iot.gateway.bean.User;
import com.gientech.iot.gateway.constant.GatewayConstants;
import com.gientech.iot.gateway.service.UserService;
import com.gientech.iot.gateway.util.TokenUtils;
import com.gientech.iot.redis.utils.RedisUtils;
import com.gientech.iot.web.commons.http.GientechResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @description: 自定义身份验证成功处理程序
 * @author: 王强
 * @dateTime: 2022-10-26 20:32:59
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final RedisUtils redisUtils;

    private final UserService userService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("=====================认证成功=====================");
        User user = (User) authentication.getPrincipal();
        // 通过工具生成token
        String token = TokenUtils.createToken(user);
        // 生成存入redis的key
        String redisTokenKey = String.format(GatewayConstants.AuthConstant.KEY_FORMAT, user.getUserId());
        // 将生成的jwt token使用MD5加密后作为value 存入redis 并设置过期时间
        redisUtils.setEx(redisTokenKey, MD5Utils.encrypt(token), GatewayConstants.AuthConstant.TOKEN_EXPIRED, TimeUnit.SECONDS);
        // 封装返回的token信息
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo();
        accessTokenInfo.setAccessToken(token);
        accessTokenInfo.setTokenType(GatewayConstants.AuthConstant.BEARER.trim());
        accessTokenInfo.setExpiresIn(GatewayConstants.AuthConstant.TOKEN_EXPIRED);
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        GientechResponse<AccessTokenInfo> res = GientechResponse.success(accessTokenInfo);
        String json = JSON.toJSONString(res.getBody());
        try {
            exchange.getRequest().mutate()
                    .header("USER_INFO", URLEncoder.encode(JSON.toJSONString(user), "UTF-8"))
                    .build();
        } catch (Exception ignored) {}
        // 记录登陆日志
        userService.loginSuccess(exchange.getRequest().getHeaders());
        return response.writeAndFlushWith(
                Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8)))));
    }
}
