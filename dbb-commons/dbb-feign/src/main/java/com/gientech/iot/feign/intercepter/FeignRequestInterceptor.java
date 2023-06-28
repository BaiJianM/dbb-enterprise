package com.gientech.iot.feign.intercepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @description: Feign请求拦截器（解决header丢失问题）
 * @author: 白剑民
 * @dateTime: 2023-02-22 22:20:46
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 获取当前请求的 RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            // 从 RequestAttributes 中获取请求头信息
            String headerValue = (String) requestAttributes.getAttribute("USER_INFO", RequestAttributes.SCOPE_REQUEST);
            if (headerValue != null) {
                // 添加自定义请求头到 Feign 请求模板中
                template.header("USER_INFO", headerValue);
            }
        }
    }
}
