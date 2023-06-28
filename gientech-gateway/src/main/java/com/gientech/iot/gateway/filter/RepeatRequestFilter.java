package com.gientech.iot.gateway.filter;

import com.gientech.iot.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description: 全局接口幂等过滤器。
 * @author: 王强
 * @dateTime: 2022-10-17 16:04:36
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RepeatRequestFilter implements GlobalFilter, Ordered {

    private final RedisUtils redisUtils;

    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 定义接口重复请求异常，正常success返回，不做异常返回，页面无异常提示，接口响应中显示提示信息"请求过于频繁"
//        GientechException repeatException = new GientechException(ResponseCode.SUCCESS.getCode(), AuthErrorConstant.REPEAT_REQUEST);
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//        String method = request.getMethodValue();
//        String ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
//        Optional<String> token = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
//        // 网关白名单放行的接口则不存在token，直接放行
//        if (!token.isPresent()) {
//            return chain.filter(exchange);
//        }
//        // POST请求幂等校验
//        if (HttpMethod.POST.name().equals(method)) {
//            Object postBody = Optional.ofNullable(exchange.getAttribute("POST_BODY")).orElse("");
//            // 使用(请求URL + MD5(token + 请求参数))进行唯一校验
//            String requestKey =
//                    String.format(AuthConstant.REPEAT_FORMAT, path, MD5Utils.getMd5Value(token.get() + postBody));
//            if (redisUtils.hasKey(requestKey).orElse(Boolean.FALSE)) {
//                log.info("接口重复请求: {} {}", method, path);
//                // 抛出异常，由网关全局异常类拦截处理 GlobalErrorWebExceptionHandler
//                return Mono.error(repeatException);
//            }
//            // 接口做1秒的防重复点击
//            redisUtils.setEx(requestKey, ipAddress, 1, TimeUnit.SECONDS);
//        }
//        // GET请求幂等校验
//        else if (HttpMethod.GET.name().equals(method)) {
//            Object getParams = Optional.ofNullable(exchange.getAttribute("GET_PARAMS")).orElse("");
//            // 使用(请求URL + MD5(token + 请求参数))进行唯一校验
//            String requestKey =
//                    String.format(AuthConstant.REPEAT_FORMAT, path, MD5Utils.getMd5Value(token.get() + getParams));
//            if (redisUtils.hasKey(requestKey).orElse(Boolean.FALSE)) {
//                log.info("接口重复请求: {} {}", method, path);
//                // 抛出异常，由网关全局异常类拦截处理 GlobalErrorWebExceptionHandler
//                return Mono.error(repeatException);
//            }
//            // 接口做1秒的防重复点击
//            redisUtils.setEx(requestKey, ipAddress, 1, TimeUnit.SECONDS);
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
