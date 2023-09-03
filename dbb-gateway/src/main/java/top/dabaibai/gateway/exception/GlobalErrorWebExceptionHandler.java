package top.dabaibai.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.http.DbbResponse;

/**
 * @description: 网关全局异常处理
 * @author: 白剑民
 * @dateTime: 2022-10-25 21:01:03
 */
@Slf4j
@Order(-1)
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(throwable);
        }
        // 设置返回类型 application/json
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) throwable;
            response.setStatusCode(exception.getStatus());
        }
        // 自定义异常处理
        if (throwable instanceof DbbException) {
            DbbException exception = (DbbException) throwable;
            return response.writeWith(Mono.fromSupplier(() -> {
                DataBufferFactory bufferFactory = response.bufferFactory();
                try {
                    return bufferFactory.wrap(objectMapper.writeValueAsBytes(DbbResponse.fail("", exception).getBody()));
                } catch (JsonProcessingException e) {
                    log.error("网关全局异常拦截响应异常", throwable);
                    return bufferFactory.wrap(new byte[0]);
                }
            }));
        }
        // 默认异常处理
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(
                        objectMapper.writeValueAsBytes(DbbResponse.fail(throwable.getMessage()).getBody()));
            } catch (JsonProcessingException e) {
                log.error("网关全局异常拦截响应异常", throwable);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}