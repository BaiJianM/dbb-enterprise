package com.gientech.iot.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.gientech.iot.gateway.constant.GatewayConstants;
import com.gientech.iot.web.commons.http.GientechResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.nio.charset.StandardCharsets;

/**
 * @description: 用户身份认证成功，但无访问该资源权限的处理类
 * @author: 白剑民
 * @dateTime: 2022-10-17 21:46:28
 */
@Slf4j
@Component
public class CustomServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        log.info("=====================鉴权失败=====================");
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.FORBIDDEN);
        GientechResponse<Integer> res = GientechResponse.fail(GatewayConstants.AuthErrorConstant.FORBIDDEN);
        String json = JSON.toJSONString(res.getBody());
        return response.writeAndFlushWith(
                Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8)))));
    }
}
