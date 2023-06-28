package com.gientech.iot.gateway.manager.authentication.account;

import com.gientech.iot.gateway.manager.authentication.BaseAuthentication;
import com.gientech.iot.gateway.manager.authentication.BaseAuthenticationConverter;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 账号认证信息转换器 exchange中获取登录信息
 * @author: 王强
 * @dateTime: 2022-10-26 13:32:40
 */
@Data
@Component
public class AccountAuthenticationConverter implements BaseAuthenticationConverter {

    @Override
    public Mono<BaseAuthentication> convert(ServerWebExchange exchange) {
        return exchange.getFormData().map(this::createAuthentication);
    }

    private AccountAuthentication createAuthentication(MultiValueMap<String, String> data) {
        Map<String, String> map = data.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> p.getValue().get(0)));
        return (AccountAuthentication) mapToBean(map, AccountAuthentication.class);
    }
}
