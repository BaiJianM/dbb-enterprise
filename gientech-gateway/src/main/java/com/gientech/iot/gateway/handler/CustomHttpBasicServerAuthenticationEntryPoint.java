package com.gientech.iot.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.gateway.constant.GatewayConstants;
import com.gientech.iot.web.commons.http.GientechResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.StandardCharsets;

/**
 * @description: 未登录访问资源时的处理类
 * @author: 王强
 * @dateTime: 2022-10-17 21:46:28
 */
@Slf4j
@Component
public class CustomHttpBasicServerAuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.info("=====================未登录=====================");
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        GientechResponse<Integer> res = GientechResponse.fail(GatewayConstants.AuthErrorConstant.UN_LOGIN);
        String json = JSON.toJSONString(res.getBody());
        return response.writeAndFlushWith(
                Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8)))));
    }
}
