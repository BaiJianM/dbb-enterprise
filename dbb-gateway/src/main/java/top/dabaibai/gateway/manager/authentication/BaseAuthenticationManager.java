package top.dabaibai.gateway.manager.authentication;

import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @description: 身份验证管理器基类
 * @author: 白剑民
 * @dateTime: 2022-10-27 10:45:48
 */
public interface BaseAuthenticationManager<T extends BaseAuthentication> {

    /**
     * @param authentication 身份信息
     * @description: 进行身份验证
     * @author: 白剑民
     * @date: 2022-10-27 10:45:53
     * @return: Mono<Authentication>
     * @version: 1.0
     */
    Mono<Authentication> authenticate(T authentication);
}
