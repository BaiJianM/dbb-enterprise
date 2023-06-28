package top.dabaibai.gateway.manager.authentication;

import reactor.core.publisher.Mono;

/**
 * @description: 身份验证管理器解析器基类
 * @author: 白剑民
 * @dateTime: 2022-10-27 10:45:40
 */
public interface BaseAuthenticationManagerResolver<C> {

    /**
     * @param context 上下文
     * @description: 解析
     * @author: 白剑民
     * @date: 2022-10-27 10:45:57
     * @return: Mono<BaseAuthenticationManager>
     * @version: 1.0
     */
    Mono<BaseAuthenticationManager> resolve(C context);
}
