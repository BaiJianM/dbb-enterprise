package com.gientech.iot.gateway.manager.authentication;

import com.gientech.iot.gateway.util.ValidateUtil;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @description: 身份验证信息转换器基类
 * @author: 王强
 * @dateTime: 2022-10-26 13:32:40
 */
public interface BaseAuthenticationConverter {

    /**
     * @param exchange w请求数据对象
     * @description: 将请求中的参数转换成BaseAuthentication对象
     * @author: 王强
     * @date: 2022-10-27 10:46:04
     * @return: Mono<BaseAuthentication>
     * @version: 1.0
     */
    Mono<BaseAuthentication> convert(ServerWebExchange exchange);

    /**
     * @param map       Map对象
     * @param beanClass 继承BaseAuthentication的泛型对象
     * @description: 将Map对象转换成BaseAuthentication对象
     * @author: 王强
     * @date: 2022-10-27 11:22:36
     * @return: Object
     * @version: 1.0
     */
    @SneakyThrows
    default <T extends BaseAuthentication> Object mapToBean(Map<String, String> map, Class<T> beanClass) {
        if (map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        ValidateUtil.valid(obj);
        return obj;
    }
}
