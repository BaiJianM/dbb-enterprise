package com.gientech.iot.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.core.Constants;
import com.gientech.iot.gateway.bean.TokenUser;
import com.gientech.iot.gateway.constant.GatewayConstants;
import com.gientech.iot.gateway.service.UserService;
import com.gientech.iot.redis.utils.RedisUtils;
import com.gientech.iot.web.commons.http.GientechResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.StandardCharsets;

/**
 * @description: 用户登出成功后的处理类
 * @author: 白剑民
 * @dateTime: 2022-10-17 21:46:41
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final RedisUtils redisUtils;

    private final UserService userService;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        // 删除redis中用户的token信息
        TokenUser user = (TokenUser) authentication.getPrincipal();
        String redisTokenKey = String.format(GatewayConstants.AuthConstant.KEY_FORMAT, user.getUserId());
        redisUtils.delete(redisTokenKey);
        // 删除redis中用户的缓存信息
        String userKey = Constants.LoginUser.LOGIN_USER_PREFIX + user.getUserId();
        redisUtils.delete(userKey);
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        String json = JSON.toJSONString(GientechResponse.success().getBody());
        // 记录登出日志
        userService.logoutSuccess(exchange.getRequest().getHeaders());
        log.info("=====================登出成功=====================");
        return response.writeAndFlushWith(
                Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8)))));
    }
}
